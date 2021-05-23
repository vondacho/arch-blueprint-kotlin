package edu.software.craftsmanship.blueprint.appl.handler

import com.structurizr.annotation.Component
import edu.software.craftsmanship.blueprint.appl.model.ClientInput
import edu.software.craftsmanship.blueprint.domain.client.model.Client
import edu.software.craftsmanship.blueprint.domain.client.model.ClientId
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component(description = "Handles commands on client entities")
interface ClientCommandHandler {

    fun get(id: ClientId, activatedOnly: Boolean = true): Mono<Client>
    fun find(name: String? = null, activatedOnly: Boolean = true): Flux<Client>
    fun search(term: String, activatedOnly: Boolean = true): Flux<Client>
    fun add(input: ClientInput): Mono<Client>
    fun update(id: ClientId, input: ClientInput): Mono<Int>
    fun delete(id: ClientId): Mono<Int>
    fun restore(id: ClientId): Mono<Client>
}
