package edu.software.craftsmanship.blueprint.at.client.api

import edu.software.craftsmanship.blueprint.at.client.types.TestClient
import edu.software.craftsmanship.blueprint.at.client.types.TestClientInput
import edu.software.craftsmanship.blueprint.at.common.types.TestResult
import org.springframework.http.HttpStatus
import reactor.core.publisher.Mono

interface ClientApiClient {

    fun addClient(input: TestClientInput): Mono<TestResult<TestClient, HttpStatus>>
    fun getClient(id: String): Mono<TestResult<TestClient, HttpStatus>>
    fun searchClients(name: String): Mono<TestResult<List<TestClient>, HttpStatus>>
    fun updateClient(id: String, input: TestClientInput): Mono<TestResult<Unit, HttpStatus>>
    fun deleteClient(id: String): Mono<TestResult<Unit, HttpStatus>>
    fun restoreClient(id: String): Mono<TestResult<Unit, HttpStatus>>
}
