package edu.software.craftsmanship.blueprint.data.adapter

import com.structurizr.annotation.Component
import com.structurizr.annotation.UsesContainer
import edu.software.craftsmanship.blueprint.data.adapter.r2dbc.R2dbcClientRepository
import edu.software.craftsmanship.blueprint.data.model.ClientData
import edu.software.craftsmanship.blueprint.domain.client.model.Client
import edu.software.craftsmanship.blueprint.domain.client.model.ClientId
import edu.software.craftsmanship.blueprint.domain.client.repository.ClientRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.function.Supplier

@Component(description = "Client data storage")
@UsesContainer(name = "database-server", description = "Reads and writes", technology = "R2DBC")
class ClientRepositoryAdapter(
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

    override fun delete(client: Client): Mono<Client> {
        val data = client.toDataEntity(timestampSupplier.get(), activated = false)
        return save(data)
    }

    override fun restore(client: Client): Mono<Int> {
        val data = client.toDataEntity(timestampSupplier.get(), activated = true)
        return repository.save(data).map { 1 }
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
