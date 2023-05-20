package edu.obya.blueprint.client.adapter.r2dbc

import com.structurizr.annotation.Component
import com.structurizr.annotation.UsesContainer
import edu.obya.blueprint.client.domain.model.Client
import edu.obya.blueprint.client.domain.model.ClientId
import edu.obya.blueprint.client.domain.service.ClientRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.function.Supplier

@Component(description = "Client data storage")
@UsesContainer(name = "database-server", description = "Reads and writes", technology = "R2DBC")
class ClientRepositoryR2dbcAdapter(
    private val repository: R2dbcClientRepository,
    private val idSupplier: Supplier<ClientId>,
    private val timestampSupplier: Supplier<LocalDateTime>
) : ClientRepository {

    override fun save(client: Client.State): Mono<Client> {
        return save(idSupplier.get(), client)
    }

    override fun save(id: ClientId, client: Client.State): Mono<Client> {
        val data = client.toDataEntity(id, timestampSupplier.get())
        return save(data)
    }

    override fun save(client: Client): Mono<Client> {
        val data = client.toDataEntity(timestampSupplier.get())
        return save(data)
    }

    override fun findOne(id: ClientId, activatedOnly: Boolean): Mono<Client> =
        (if (activatedOnly) repository.activeById(id) else repository.findById(id))
            .map { it.toDomainEntity() }

    override fun findByName(name: String, strict: Boolean, activatedOnly: Boolean): Flux<Client> =
        (if (strict) {
            if (activatedOnly) repository.activeByNameStrict(name)
            else repository.byNameStrict(name)
        } else {
            if (activatedOnly) repository.activeByNameNotStrict(name)
            else repository.byNameNotStrict(name)
        })
            .map { it.toDomainEntity() }

    override fun findAll(activatedOnly: Boolean): Flux<Client> =
        (if (activatedOnly) repository.activeOnes() else repository.findAll())
            .map { it.toDomainEntity() }

    override fun remove(client: Client): Mono<Client> {
        val data = client.toDataEntity(timestampSupplier.get(), activated = false)
        return save(data)
    }

    private fun save(data: ClientData) = repository.save(data).map { it.toDomainEntity() }

    private fun Client.State.toDataEntity(id: ClientId, createdAt: LocalDateTime) = ClientData(
        _id = id,
        name = name,
        age = age,
        createdAt = createdAt,
        modifiedAt = createdAt,
        isNewInstance = true
    )

    private fun Client.toDataEntity(modifiedAt: LocalDateTime, activated: Boolean = true) = ClientData(
        _id = id,
        name = state.name,
        age = state.age,
        createdAt = state.createdAt!!,
        modifiedAt = modifiedAt,
        activated = activated
    )

    private fun ClientData.toDomainEntity() = Client(
        id = this.id,
        state = Client.State(
            name = name,
            age = age,
            createdAt = createdAt,
            activated = activated
        )
    )
}
