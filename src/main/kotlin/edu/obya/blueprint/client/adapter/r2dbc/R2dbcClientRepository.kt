package edu.obya.blueprint.client.adapter.r2dbc

import edu.obya.blueprint.client.domain.model.ClientId
import org.springframework.data.domain.Sort
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface R2dbcClientRepository : R2dbcRepository<ClientData, ClientId> {

    fun findByActivated(activated: Boolean = true, sort: Sort = defaultSort): Flux<ClientData>
    fun findByIdAndActivated(id: ClientId, activated: Boolean = true): Mono<ClientData>
    fun findByNameIgnoreCase(name: String, sort: Sort = defaultSort): Flux<ClientData>
    fun findByNameIgnoreCaseContaining(name: String, sort: Sort = defaultSort): Flux<ClientData>

    fun findByNameIgnoreCaseAndActivated(
        name: String,
        activated: Boolean = true,
        sort: Sort = defaultSort
    ): Flux<ClientData>

    fun findByNameIgnoreCaseContainingAndActivated(
        name: String,
        activated: Boolean = true,
        sort: Sort = defaultSort
    ): Flux<ClientData>

    fun activeOnes() = findByActivated()
    fun activeById(id: ClientId) = findByIdAndActivated(id)
    fun activeByNameStrict(name: String) = findByNameIgnoreCaseAndActivated(name)
    fun activeByNameNotStrict(name: String) = findByNameIgnoreCaseContainingAndActivated(name)
    fun byNameStrict(name: String) = findByNameIgnoreCase(name)
    fun byNameNotStrict(name: String) = findByNameIgnoreCaseContaining(name)
}
