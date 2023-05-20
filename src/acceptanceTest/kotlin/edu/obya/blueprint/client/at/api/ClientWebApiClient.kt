package edu.obya.blueprint.client.at.api

import edu.obya.blueprint.client.adapter.rest.ClientOutput
import edu.obya.blueprint.client.at.TestWebUser.TEST_ADMIN_NAME
import edu.obya.blueprint.client.at.TestWebUser.TEST_ADMIN_PASSWORD
import edu.obya.blueprint.client.at.TestWebUser.TEST_USER_NAME
import edu.obya.blueprint.client.at.TestWebUser.TEST_USER_PASSWORD
import edu.obya.blueprint.client.at.types.TestClient
import edu.obya.blueprint.client.at.types.TestClientInput
import edu.obya.blueprint.client.at.types.TestResult
import edu.obya.blueprint.client.at.types.toTestClient
import edu.obya.blueprint.client.at.types.toTestClientList
import edu.obya.blueprint.client.domain.model.ClientId
import edu.obya.blueprint.common.at.context.TestContext.atOrDefault
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class ClientWebApiClient(private val webClient: WebClient) : ClientApiClient {

    override fun addClient(input: TestClientInput): Mono<TestResult<ClientId, HttpStatus>> {
        return webClient.post().uri("clients")
            .bodyValue(input)
            .header(HttpHeaders.AUTHORIZATION, basicAuthHeader(
                atOrDefault("userId", TEST_USER_NAME),
                atOrDefault("userPassword", TEST_USER_PASSWORD)))
            .exchangeToMono { response ->
                if (response.statusCode().is2xxSuccessful)
                    response.bodyToMono(String::class.java)
                        .map { TestResult(ClientId.fromString(it), response.statusCode()) }
                else handleFailure(response)
            }
    }

    override fun getClient(id: ClientId): Mono<TestResult<TestClient, HttpStatus>> {
        return webClient.get().uri("clients/$id")
            .header(HttpHeaders.AUTHORIZATION, basicAuthHeader(
                atOrDefault("userId", TEST_USER_NAME),
                atOrDefault("userPassword", TEST_USER_PASSWORD)))
            .exchangeToMono { response ->
                if (response.statusCode().is2xxSuccessful)
                    response.bodyToMono(ClientOutputType())
                        .map { TestResult(it.toTestClient(), response.statusCode()) }
                else handleFailure(response)
            }
    }

    override fun searchClients(name: String): Mono<TestResult<List<TestClient>, HttpStatus>> {
        return webClient.get().uri("clients?name=$name")
            .header(HttpHeaders.AUTHORIZATION, basicAuthHeader(
                atOrDefault("userId", TEST_USER_NAME),
                atOrDefault("userPassword", TEST_USER_PASSWORD)))
            .exchangeToMono { response ->
                if (response.statusCode().is2xxSuccessful) {
                    response.bodyToMono(ClientOutputArrayType())
                        .map { TestResult(it.toTestClientList(), response.statusCode()) }
                }
                else handleFailure(response)
            }
    }

    override fun updateClient(id: ClientId, input: TestClientInput): Mono<TestResult<Unit, HttpStatus>> {
        return webClient.put().uri("clients/$id")
            .bodyValue(input)
            .header(HttpHeaders.AUTHORIZATION, basicAuthHeader(
                atOrDefault("userId", TEST_USER_NAME),
                atOrDefault("userPassword", TEST_USER_PASSWORD)))
            .exchangeToMono { Mono.just(TestResult(status = it.statusCode())) }
    }

    override fun deleteClient(id: ClientId): Mono<TestResult<Unit, HttpStatus>> {
        return webClient.delete().uri("clients/$id")
            .header(HttpHeaders.AUTHORIZATION, basicAuthHeader(
                atOrDefault("userId", TEST_ADMIN_NAME),
                atOrDefault("userPassword", TEST_ADMIN_PASSWORD)))
            .exchangeToMono { Mono.just(TestResult(status = it.statusCode())) }
    }

    override fun restoreClient(id: ClientId): Mono<TestResult<Unit, HttpStatus>> {
        return webClient.post().uri("clients/$id:restore")
            .header(HttpHeaders.AUTHORIZATION, basicAuthHeader(
                atOrDefault("userId", TEST_ADMIN_NAME),
                atOrDefault("userPassword", TEST_ADMIN_PASSWORD)))
            .exchangeToMono { Mono.just(TestResult(status = it.statusCode())) }
    }

    private class ClientIdType : ParameterizedTypeReference<ClientId>()
    private class ClientOutputType : ParameterizedTypeReference<ClientOutput>()
    private class ClientOutputArrayType : ParameterizedTypeReference<Array<ClientOutput>>()
}

@OptIn(ExperimentalEncodingApi::class)
fun basicAuthHeader(username: String, password: String): String {
    val token = Base64.encode("$username:$password".toByteArray())
    return "Basic $token"
}
