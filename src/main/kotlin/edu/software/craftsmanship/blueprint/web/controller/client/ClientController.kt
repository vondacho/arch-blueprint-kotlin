package edu.software.craftsmanship.blueprint.web.controller.client

import com.structurizr.annotation.Component
import com.structurizr.annotation.UsedByContainer
import com.structurizr.annotation.UsesComponent
import edu.software.craftsmanship.blueprint.appl.handler.ClientCommandHandler
import edu.software.craftsmanship.blueprint.appl.model.ClientInput
import edu.software.craftsmanship.blueprint.domain.client.model.ClientId
import edu.software.craftsmanship.blueprint.web.exception.toProblem
import edu.software.craftsmanship.blueprint.web.model.EmptyOutput
import edu.software.craftsmanship.blueprint.web.model.ListOutput
import edu.software.craftsmanship.blueprint.web.model.Output
import edu.software.craftsmanship.blueprint.web.model.toOutput
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@Component(description = "API for client management", technology = "Kotlin/Webflux")
@UsedByContainer(name = "Service", description = "Calls", technology = "HTTP/REST")
@RestController
class ClientController(
    @UsesComponent(description = "Delegates commands to")
    private val handler: ClientCommandHandler
) {
    private val log = KotlinLogging.logger {}

    @PostMapping("/clients")
    fun add(@RequestBody input: ClientInput): Mono<ResponseEntity<Output>> {
        return handler.add(input)
            .map { status(HttpStatus.CREATED).body(it.toOutput()) }
            .onErrorMap { it.toProblem(log) }
    }

    @GetMapping("/clients/{id}")
    fun get(
        @PathVariable id: ClientId,
        @RequestParam(required = false) activatedOnly: Boolean?
    ): Mono<ResponseEntity<Output>> {
        return handler.get(id, activatedOnly ?: true)
            .map { ok(it.toOutput()) }
            .onErrorMap { it.toProblem(log) }
    }

    @GetMapping("/clients/search")
    fun search(
        @RequestParam term: String,
        @RequestParam(required = false) activatedOnly: Boolean?
    ): Mono<ResponseEntity<Output>> {
        return handler.search(term, activatedOnly ?: true)
            .collectList()
            .map { items -> ok(ListOutput(items) as Output) }
            .onErrorMap { it.toProblem(log) }
    }

    @GetMapping("/clients")
    fun findAll(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) activatedOnly: Boolean?
    ): Mono<ResponseEntity<Output>> {
        return handler.find(name, activatedOnly ?: true).map { it.toOutput() }
            .collectList()
            .map { items -> ok(ListOutput(items) as Output) }
            .onErrorMap { it.toProblem(log) }
    }

    @PutMapping("/clients/{id}")
    fun update(@PathVariable id: ClientId, @RequestBody input: ClientInput): Mono<ResponseEntity<Output>> {
        return handler.update(id, input)
            .map { status(HttpStatus.NO_CONTENT).body(EmptyOutput() as Output) }
            .onErrorMap { it.toProblem(log) }
    }

    @DeleteMapping("/clients/{id}")
    fun delete(@PathVariable id: ClientId): Mono<ResponseEntity<Output>> {
        return handler.delete(id)
            .map { status(HttpStatus.NO_CONTENT).body(EmptyOutput() as Output) }
            .onErrorMap { it.toProblem(log) }
    }

    @PostMapping("/clients/{id}/restore")
    fun restore(@PathVariable id: ClientId): Mono<ResponseEntity<Output>> {
        return handler.restore(id)
            .map { ok(it.toOutput()) }
            .onErrorMap { it.toProblem(log) }
    }
}
