package edu.obya.blueprint.client.config

import edu.obya.blueprint.client.domain.service.ClientAlreadyExistsException
import edu.obya.blueprint.client.domain.service.ClientNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ServerWebExchange
import org.zalando.problem.Problem
import org.zalando.problem.Status
import org.zalando.problem.spring.webflux.advice.AdviceTrait
import reactor.core.publisher.Mono
import java.net.URI

interface ClientAdviceTrait: AdviceTrait {

    @ExceptionHandler
    fun handleThrowable(
        throwable: ClientNotFoundException,
        request: ServerWebExchange): Mono<ResponseEntity<Problem>> {
        return create(
            throwable,
                Problem.builder()
                        .withType(URI.create("https://obya.edu/not-found"))
                        .withStatus(Status.NOT_FOUND)
                        .withTitle("Client not found")
                        .withDetail(throwable.message)
                        .build(),
                request)
    }

    @ExceptionHandler
    fun handleThrowable(
        throwable: ClientAlreadyExistsException,
        request: ServerWebExchange): Mono<ResponseEntity<Problem>> {
        return create(
            throwable,
                Problem.builder()
                        .withType(URI.create("https://obya.edu/already-exist"))
                        .withStatus(Status.BAD_REQUEST)
                        .withTitle("Client exists")
                        .withDetail(throwable.message)
                        .build(),
                request)
    }
}
