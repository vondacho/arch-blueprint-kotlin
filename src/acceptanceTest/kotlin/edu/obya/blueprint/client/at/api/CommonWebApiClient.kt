package edu.obya.blueprint.client.at.api

import edu.obya.blueprint.client.at.types.TestResult
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ClientResponse
import reactor.core.publisher.Mono

fun <T> handleFailure(response: ClientResponse): Mono<TestResult<T, HttpStatus>> =
    when (response.statusCode()) {
        HttpStatus.FORBIDDEN, HttpStatus.UNAUTHORIZED -> Mono.just(TestResult(status = response.statusCode()))
        else -> response.bodyToMono(EmptyOutputType()).map { TestResult(status = response.statusCode()) }
    }
