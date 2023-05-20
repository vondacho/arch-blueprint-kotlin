package edu.obya.blueprint.client.application

import com.structurizr.annotation.Component
import com.structurizr.annotation.UsesComponent
import edu.obya.blueprint.client.domain.model.Client
import edu.obya.blueprint.client.domain.model.ClientId
import edu.obya.blueprint.client.domain.service.ClientAlreadyExistsException
import edu.obya.blueprint.client.domain.service.ClientNotFoundException
import edu.obya.blueprint.client.domain.service.ClientRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Component(description = "Manages client entities")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
@Transactional
class ClientUseCase(
    @UsesComponent(description = "Accesses client data")
    private val clientRepository: ClientRepository
): ClientCommandHandler {

    override fun add(input: ClientInput): Mono<ClientId> =
        checkNoDuplicate(input.name)
            .flatMap { clientRepository.save(input.toState()) }
            .map { it.id }

    @Transactional(readOnly = true)
    override fun get(id: ClientId, activatedOnly: Boolean): Mono<Client> = checkExists(id, activatedOnly)

    @Transactional(readOnly = true)
    override fun find(name: String?, activatedOnly: Boolean): Flux<Client> = when {
        name?.isNotBlank() == true -> clientRepository.findByName(name, activatedOnly = activatedOnly)
        else -> clientRepository.findAll(activatedOnly = activatedOnly)
    }

    @Transactional(readOnly = true)
    override fun search(term: String, activatedOnly: Boolean) = find(name = term, activatedOnly)

    override fun replace(id: ClientId, input: ClientInput): Mono<ClientId> =
        checkExists(id)
            .flatMap { client -> checkNoDuplicate(input.name, id).map { client } }
            .flatMap { client -> clientRepository.save(input.toClient(id, client.state.createdAt!!)).thenReturn(id) }

    @PreAuthorize("hasRole('ADMIN')")
    override fun remove(id: ClientId): Mono<ClientId> =
        checkExists(id).flatMap { clientRepository.remove(it).map { id } }

    @PreAuthorize("hasRole('ADMIN')")
    override fun restore(id: ClientId): Mono<ClientId> =
        checkDeactivated(id).flatMap { clientRepository.save(it.activate()).map { id } }

    private fun checkExists(id: ClientId, activatedOnly: Boolean = true): Mono<Client> =
        clientRepository.findOne(id, activatedOnly)
            .switchIfEmpty(Mono.error(ClientNotFoundException(id)))

    private fun checkDeactivated(id: ClientId): Mono<Client> =
        clientRepository.findOne(id, activatedOnly = false)
            .filter { !it.state.activated }
            .switchIfEmpty(Mono.error(ClientNotFoundException(id)))

    private fun checkNoDuplicate(name: String, id: ClientId? = null): Mono<List<Client>> =
        when {
            name.isNotBlank() -> clientRepository.findByName(name, strict = true)
            else -> clientRepository.findAll()
        }
            .filter { client -> id?.let { client.id != it } ?: true }
            .collectList()
            .flatMap {
                if (it.isNotEmpty()) Mono.error(ClientAlreadyExistsException(name))
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
