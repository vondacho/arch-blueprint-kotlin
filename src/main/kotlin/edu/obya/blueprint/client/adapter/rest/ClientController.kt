package edu.obya.blueprint.client.adapter.rest

import com.structurizr.annotation.Component
import com.structurizr.annotation.UsedByContainer
import com.structurizr.annotation.UsesComponent
import edu.obya.blueprint.client.application.ClientCommandHandler
import edu.obya.blueprint.client.application.ClientInput
import edu.obya.blueprint.client.domain.model.ClientId
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.TEXT_PLAIN_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
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

    @PostMapping("/clients", produces = [TEXT_PLAIN_VALUE])
    fun add(@RequestBody input: ClientInput): Mono<ResponseEntity<String>> {
        return handler.add(input)
            .map { status(HttpStatus.CREATED)
                .header(CONTENT_TYPE, TEXT_PLAIN_VALUE)
                .body(it.toString())
            }
    }

    @GetMapping("/clients/{id}")
    fun get(
        @PathVariable id: ClientId,
        @RequestParam(required = false) activatedOnly: Boolean?
    ): Mono<ResponseEntity<ClientOutput>> {
        return handler.get(id, activatedOnly ?: true)
            .map { ok(it.toOutput()) }
    }

    @GetMapping("/clients:search")
    fun search(
        @RequestParam term: String,
        @RequestParam(required = false) activatedOnly: Boolean?
    ): Mono<ResponseEntity<List<ClientOutput>>> {
        return handler.search(term, activatedOnly ?: true).map { it.toOutput() }
            .collectList()
            .map { items -> ok(items) }
    }

    @GetMapping("/clients")
    fun findAll(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) activatedOnly: Boolean?
    ): Mono<ResponseEntity<List<ClientOutput>>> {
        return handler.find(name, activatedOnly ?: true).map { it.toOutput() }
            .collectList()
            .map { items -> ok(items) }
    }

    @PutMapping("/clients/{id}")
    fun update(@PathVariable id: ClientId, @RequestBody input: ClientInput): Mono<ResponseEntity<Unit>> {
        return handler.replace(id, input).map { noContent().build() }
    }

    @DeleteMapping("/clients/{id}")
    fun delete(@PathVariable id: ClientId): Mono<ResponseEntity<Unit>> {
        return handler.remove(id).map { noContent().build() }
    }

    @PostMapping("/clients/{id}:restore")
    fun restore(@PathVariable id: ClientId): Mono<ResponseEntity<Unit>> {
        return handler.restore(id).map { noContent().build() }
    }
}
