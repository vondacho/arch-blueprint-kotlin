package edu.obya.blueprint.client.at.api

import edu.obya.blueprint.client.at.types.TestClient
import edu.obya.blueprint.client.at.types.TestClientInput
import edu.obya.blueprint.client.at.types.TestResult
import edu.obya.blueprint.client.domain.model.ClientId
import org.springframework.http.HttpStatus
import reactor.core.publisher.Mono

interface ClientApiClient {

    fun addClient(input: TestClientInput): Mono<TestResult<ClientId, HttpStatus>>
    fun getClient(id: ClientId): Mono<TestResult<TestClient, HttpStatus>>
    fun searchClients(name: String): Mono<TestResult<List<TestClient>, HttpStatus>>
    fun updateClient(id: ClientId, input: TestClientInput): Mono<TestResult<Unit, HttpStatus>>
    fun deleteClient(id: ClientId): Mono<TestResult<Unit, HttpStatus>>
    fun restoreClient(id: ClientId): Mono<TestResult<Unit, HttpStatus>>
}
