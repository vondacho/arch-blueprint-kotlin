package edu.obya.blueprint.client.domain.service

import com.structurizr.annotation.Component
import edu.obya.blueprint.client.domain.model.Client
import edu.obya.blueprint.client.domain.model.ClientId
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component(description = "Client data storage")
interface ClientRepository {

    fun save(client: Client.State): Mono<Client>

    fun save(id: ClientId, client: Client.State): Mono<Client>

    fun save(client: Client): Mono<Client>

    fun findOne(id: ClientId, activatedOnly: Boolean = true): Mono<Client>

    fun findByName(name: String, strict: Boolean = false, activatedOnly: Boolean = true): Flux<Client>

    fun findAll(activatedOnly: Boolean = true): Flux<Client>

    fun remove(client: Client): Mono<Client>
}
