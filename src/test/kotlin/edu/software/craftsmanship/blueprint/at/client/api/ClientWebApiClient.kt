package edu.software.craftsmanship.blueprint.at.client.api

import edu.software.craftsmanship.blueprint.at.client.types.TestClient
import edu.software.craftsmanship.blueprint.at.client.types.TestClientInput
import edu.software.craftsmanship.blueprint.at.client.types.toTestClient
import edu.software.craftsmanship.blueprint.at.client.types.toTestClientList
import edu.software.craftsmanship.blueprint.at.common.api.handleFailure
import edu.software.craftsmanship.blueprint.at.common.types.TestResult
import edu.software.craftsmanship.blueprint.web.model.ClientOutput
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class ClientWebApiClient(private val webClient: WebClient) : ClientApiClient {

    override fun addClient(input: TestClientInput): Mono<TestResult<TestClient, HttpStatus>> {
        return webClient.post().uri("clients").bodyValue(input)
            .exchangeToMono { response ->
                if (response.statusCode().is2xxSuccessful)
                    response.bodyToMono(ClientOutputType())
                        .map { TestResult(it.toTestClient(), response.statusCode()) }
                else handleFailure(response)
            }
    }

    override fun getClient(id: String): Mono<TestResult<TestClient, HttpStatus>> {
        return webClient.get().uri("clients/$id")
            .exchangeToMono { response ->
                if (response.statusCode().is2xxSuccessful)
                    response.bodyToMono(ClientOutputType())
                        .map { TestResult(it.toTestClient(), response.statusCode()) }
                else handleFailure(response)
            }
    }

    override fun searchClients(name: String): Mono<TestResult<List<TestClient>, HttpStatus>> {
        return webClient.get().uri("clients?name=$name")
            .exchangeToMono { response ->
                if (response.statusCode().is2xxSuccessful)
                    response.bodyToMono(ClientOutputArrayType())
                        .map { TestResult(it.toTestClientList(), response.statusCode()) }
                else handleFailure(response)
            }
    }

    override fun updateClient(id: String, input: TestClientInput): Mono<TestResult<Unit, HttpStatus>> {
        return webClient.put().uri("clients/$id").bodyValue(input)
            .exchangeToMono { Mono.just(TestResult(status = it.statusCode())) }
    }

    override fun deleteClient(id: String): Mono<TestResult<Unit, HttpStatus>> {
        return webClient.delete().uri("clients/$id")
            .exchangeToMono { Mono.just(TestResult(status = it.statusCode())) }
    }

    override fun restoreClient(id: String): Mono<TestResult<Unit, HttpStatus>> {
        return webClient.post().uri("clients/$id/restore")
            .exchangeToMono { Mono.just(TestResult(status = it.statusCode())) }
    }

    private class ClientOutputType : ParameterizedTypeReference<ClientOutput>()
    private class ClientOutputArrayType : ParameterizedTypeReference<Array<ClientOutput>>()
}
