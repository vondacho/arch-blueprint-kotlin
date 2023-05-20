package edu.obya.blueprint.client.application

import com.structurizr.annotation.Component
import edu.obya.blueprint.client.domain.model.Client
import edu.obya.blueprint.client.domain.model.ClientId
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component(description = "Handles commands on client entities")
interface ClientCommandHandler {

    fun get(id: ClientId, activatedOnly: Boolean = true): Mono<Client>
    fun find(name: String? = null, activatedOnly: Boolean = true): Flux<Client>
    fun search(term: String, activatedOnly: Boolean = true): Flux<Client>
    fun add(input: ClientInput): Mono<ClientId>
    fun replace(id: ClientId, input: ClientInput): Mono<ClientId>
    fun remove(id: ClientId): Mono<ClientId>
    fun restore(id: ClientId): Mono<ClientId>
}
