package edu.software.craftsmanship.blueprint.web.validation

import com.atlassian.oai.validator.OpenApiInteractionValidator
import com.atlassian.oai.validator.model.Request
import com.atlassian.oai.validator.model.SimpleRequest
import com.atlassian.oai.validator.model.SimpleResponse
import com.atlassian.oai.validator.report.ValidationReport
import edu.software.craftsmanship.blueprint.web.exception.InputProblem
import mu.KotlinLogging
import org.reactivestreams.Publisher
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import java.nio.charset.StandardCharsets

class WebExchangeValidatorWebFilter(
    private val validator: OpenApiInteractionValidator,
    private val defaultConfig: WebExchangeValidatorProperties
) : WebFilter {

    private var marginalConfig: WebExchangeValidatorPathConfig? = null

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return next(exchange, chain, pathConfiguration(exchange.request))
    }

    private fun pathConfiguration(request: ServerHttpRequest): WebExchangeValidatorPathConfig =
        marginalConfig ?: defaultConfig.matches(request.path.toString()) ?: WebExchangeValidatorPathConfig.disabled()

    private fun next(
        exchange: ServerWebExchange,
        chain: WebFilterChain,
        config: WebExchangeValidatorPathConfig
    ): Mono<Void> =
        if (config.enableRequest || config.enableResponse)
            chain.filter(
                exchange
                    .mutate()
                    .request(if (config.enableRequest) prepareRequestValidation(exchange, config) else exchange.request)
                    .response(
                        if (config.enableResponse) prepareResponseValidation(
                            exchange,
                            config
                        ) else exchange.response
                    )
                    .build()
            )
        else chain.filter(exchange)

    private fun prepareRequestValidation(exchange: ServerWebExchange, config: WebExchangeValidatorPathConfig) =
        WebRequest.of(exchange.request, validator, config.lenientMode)

    private fun prepareResponseValidation(exchange: ServerWebExchange, config: WebExchangeValidatorPathConfig) =
        WebResponse(exchange, validator, config.lenientMode)
}

internal abstract class WebRequest(
    delegate: ServerHttpRequest,
    private val validator: OpenApiInteractionValidator,
    private val lenientMode: Boolean
) : ServerHttpRequestDecorator(delegate) {

    protected val logger = KotlinLogging.logger {}

    protected fun validate(request: SimpleRequest): ValidationReport {
        logger.trace("Validating web request ${request.body}")
        return validator.validateRequest(request)
    }

    protected fun actOnReport(report: ValidationReport) {
        if (report.hasErrors()) {
            report.toInputProblem().let {
                logger.info(it.message)
                if (lenientMode.not()) {
                    throw it
                }
            }
        } else {
            logger.trace("Valid web request.")
        }
    }

    protected fun toSimpleRequestBuilder(): SimpleRequest.Builder {
        val builder = SimpleRequest.Builder(toSimpleMethod(), toSimplePath())
        queryParams.forEach { (name, values) -> builder.withQueryParam(name, values) }
        headers.forEach { name, values -> builder.withHeader(name, values) }
        return builder
    }

    companion object {
        fun of(
            delegate: ServerHttpRequest,
            validator: OpenApiInteractionValidator,
            lenientMode: Boolean = false
        ): WebRequest =
            if (delegate.hasBody()) WebRequestWithBody(delegate, validator, lenientMode)
            else WebRequestWithoutBody(delegate, validator, lenientMode)
    }
}

internal class WebRequestWithoutBody(
    delegate: ServerHttpRequest,
    validator: OpenApiInteractionValidator,
    lenientMode: Boolean
) : WebRequest(delegate, validator, lenientMode) {
    init {
        validate(toSimpleRequestBuilder().build()).also { actOnReport(it) }
    }
}

internal class WebRequestWithBody(
    delegate: ServerHttpRequest,
    validator: OpenApiInteractionValidator,
    lenientMode: Boolean
) : WebRequest(delegate, validator, lenientMode) {

    override fun getBody(): Flux<DataBuffer> {
        return DataBufferUtils.join(delegate.body)
            .doOnError { logger.warn(it) { "Could not collect the web request body." } }
            .doOnNext { buffer ->
                validate(
                    toSimpleRequestBuilder().withBody(buffer.toUTF8Text()).build()
                ).also { actOnReport(it) }
            }
            .toFlux()
    }
}

internal class WebResponse(
    private val exchange: ServerWebExchange,
    private val validator: OpenApiInteractionValidator,
    private val lenientMode: Boolean
) : ServerHttpResponseDecorator(exchange.response) {

    private val logger = KotlinLogging.logger {}

    override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
        return DataBufferUtils.join(body)
            .doOnError { logger.warn(it) { "Could not collect the web response body." } }
            .doOnNext { buffer -> validate(buffer.toUTF8Text()).also { actOnReport(it) } }
            .transformDeferred { super.writeWith(it) }
            .doOnError { logger.warn(it) { "Could not write the web response body." } }
    }

    private fun validate(body: String): ValidationReport {
        logger.trace("Validating web response $body")
        return validator.validateResponse(
            exchange.request.toSimplePath(),
            exchange.request.toSimpleMethod(),
            toSimpleResponse(body)
        )
    }

    private fun actOnReport(report: ValidationReport) {
        if (report.hasErrors()) {
            report.toInputProblem().let {
                logger.info(it.message)
                if (lenientMode.not()) {
                    throw it
                }
            }
        } else {
            logger.trace("Valid web response.")
        }
    }

    private fun toSimpleResponse(body: String): SimpleResponse {
        val status = runCatching { statusCode.value() }.getOrDefault(HttpStatus.OK.value())
        val responseBuilder = SimpleResponse.Builder(status)
        headers.forEach { name, values -> responseBuilder.withHeader(name, values) }
        return responseBuilder.withBody(body).build()
    }
}

private fun ServerHttpRequest.toSimpleMethod() = Request.Method.valueOf(method?.name.toString())
private fun ServerHttpRequest.toSimplePath() = path.pathWithinApplication().value()
private fun ServerHttpRequest.hasBody() = method
    ?.let { it == HttpMethod.POST || it == HttpMethod.PUT || it == HttpMethod.PATCH }
    ?: false

private fun ValidationReport.toInputProblem(): InputProblem {
    val messages = this.allFinalMessages().errors()
    return when {
        messages.hasKey(ValidationReportKeys.missingPath) -> InputProblem(this.asText())
        messages.hasKey(ValidationReportKeys.notAllowedContentType) -> InputProblem(this.asText())
        messages.hasKey(ValidationReportKeys.invalidJsonPayload) -> InputProblem(this.asText())
        messages.hasKey(ValidationReportKeys.processingError) -> InputProblem(this.asText())
        else -> InputProblem(this.asText())
    }
}

private fun DataBuffer.toUTF8Text() = StandardCharsets.UTF_8.decode(this.asByteBuffer()).toString()
