package edu.software.craftsmanship.blueprint.appl.usecase

import com.structurizr.annotation.Component
import com.structurizr.annotation.UsesComponent
import edu.software.craftsmanship.blueprint.appl.exception.NotFoundException
import edu.software.craftsmanship.blueprint.appl.handler.ClientCommandHandler
import edu.software.craftsmanship.blueprint.appl.model.ClientInput
import edu.software.craftsmanship.blueprint.domain.client.model.Client
import edu.software.craftsmanship.blueprint.domain.client.model.ClientId
import edu.software.craftsmanship.blueprint.domain.client.repository.ClientRepository
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Component(description = "Manages client entities")
@Transactional
class ClientUseCase(
    @UsesComponent(description = "Accesses client data")
    private val clientRepository: ClientRepository
): ClientCommandHandler {

    override fun add(input: ClientInput): Mono<Client> =
        checkNoDuplicate(input.name)
            .flatMap { clientRepository.save(input.toState()) }

    @Transactional(readOnly = true)
    override fun get(id: ClientId, activatedOnly: Boolean): Mono<Client> = checkExists(id, activatedOnly)

    @Transactional(readOnly = true)
    override fun find(name: String?, activatedOnly: Boolean): Flux<Client> = when {
        name?.isNotBlank() == true -> clientRepository.findByName(name, activatedOnly = activatedOnly)
        else -> clientRepository.findAll(activatedOnly = activatedOnly)
    }

    @Transactional(readOnly = true)
    override fun search(term: String, activatedOnly: Boolean) = find(name = term, activatedOnly)

    override fun update(id: ClientId, input: ClientInput): Mono<Int> =
        checkExists(id)
            .flatMap { client -> checkNoDuplicate(input.name, id).map { client } }
            .flatMap { client ->
                clientRepository.save(input.toClient(id, client.state.createdAt!!)).thenReturn(1) }

    override fun delete(id: ClientId): Mono<Int> =
        checkExists(id)
            .flatMap { clientRepository.delete(it).thenReturn(1) }

    override fun restore(id: ClientId): Mono<Client> =
        checkDeactivated(id)
            .flatMap { client -> clientRepository.restore(client).map { client.activate() } }

    private fun checkExists(id: ClientId, activatedOnly: Boolean = true): Mono<Client> =
        clientRepository.findOne(id, activatedOnly)
            .switchIfEmpty(Mono.error(NotFoundException("Client $id does not exist")))

    private fun checkDeactivated(id: ClientId): Mono<Client> =
        clientRepository.findOne(id, activatedOnly = false)
            .filter { !it.state.activated }
            .switchIfEmpty(Mono.error(NotFoundException("Client $id does not exist")))

    private fun checkNoDuplicate(name: String?, id: ClientId? = null): Mono<List<Client>> =
        when {
            name?.isNotBlank() == true -> clientRepository.findByName(name, strict = true)
            else -> clientRepository.findAll()
        }
            .filter { client -> id?.let { client.id != it } ?: true }
            .collectList()
            .flatMap {
                if (it.isNotEmpty()) Mono.error(IllegalStateException("Client with name $name already exists"))
                else Mono.just(it)
            }

    private fun ClientInput.toClient(id: ClientId, createdAt: LocalDateTime? = null) = Client(
        id = id,
        state = this.toState(createdAt)
    )

    private fun ClientInput.toState(createdAt: LocalDateTime? = null) = Client.State(
        name = this.name,
        age = this.age,
        createdAt = createdAt
    )
}
