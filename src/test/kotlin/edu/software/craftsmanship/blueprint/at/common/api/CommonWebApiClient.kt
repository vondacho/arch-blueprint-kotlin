package edu.software.craftsmanship.blueprint.at.common.api

import edu.software.craftsmanship.blueprint.at.common.types.TestResult
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ClientResponse
import reactor.core.publisher.Mono

fun <T> handleFailure(response: ClientResponse): Mono<TestResult<T, HttpStatus>> =
    if (response.statusCode() == HttpStatus.FORBIDDEN)
        Mono.just(TestResult(status = response.statusCode()))
    else
        response.bodyToMono(EmptyOutputType()).map { TestResult(status = response.statusCode()) }
