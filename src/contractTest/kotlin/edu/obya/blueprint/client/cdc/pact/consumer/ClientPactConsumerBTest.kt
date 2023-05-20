package edu.obya.blueprint.client.cdc.pact.consumer

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody
import au.com.dius.pact.consumer.dsl.PactBuilder
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.V4Pact
import au.com.dius.pact.core.model.annotations.Pact
import au.com.dius.pact.core.model.annotations.PactDirectory
import edu.obya.blueprint.client.adapter.rest.ClientOutput
import edu.obya.blueprint.client.cdc.TestClient.TEST_CLIENT_ID
import edu.obya.blueprint.client.cdc.TestClientOut.TEST_CLIENT_OUT
import edu.obya.blueprint.client.cdc.TestWebUserTokens.TEST_USER_TOKEN
import edu.obya.blueprint.client.domain.service.ClientNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@ExtendWith(PactConsumerTestExt::class)
@PactDirectory("src/contractTest/resources/client/cdc/pact")
class ClientPactConsumerBTest {

    @Pact(consumer = "consumerB", provider = "clientAPI")
    fun getClient(builder: PactBuilder): V4Pact? {
        return builder
            .usingLegacyDsl()
            .given("with default client")
            .uponReceiving("get default client interaction")
            .method("GET")
            .matchPath(URI_WITH_ID_REGEX, "/clients/$TEST_CLIENT_ID")
            .matchHeader(AUTHORIZATION, BASIC_AUTH_REGEX, TEST_USER_TOKEN)
            .willRespondWith()
            .status(200)
            .matchHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE, APPLICATION_JSON_VALUE)
            .body(newJsonBody { `object` ->
                `object`.uuid("id", TEST_CLIENT_ID)
                `object`.stringType("name", TEST_CLIENT_OUT.name)
                `object`.integerType("age", TEST_CLIENT_OUT.age)
                `object`.datetime("createdAt", ISO_LOCAL_DATE_TIME_FORMAT)
                `object`.booleanType("activated", TEST_CLIENT_OUT.activated)
            }.build())
            .toPact()
            .asV4Pact().get()
    }

    @Pact(consumer = "consumerB", provider = "clientAPI")
    fun getClient_noAuth(builder: PactBuilder): V4Pact? {
        return builder
            .usingLegacyDsl()
            .given("with default client")
            .uponReceiving("get default client without authentication")
            .method("GET")
            .matchPath(URI_WITH_ID_REGEX, "/clients/$TEST_CLIENT_ID")
            .willRespondWith()
            .status(401)
            .toPact()
            .asV4Pact().get()
    }

    @Test
    @PactTestFor(pactMethod = "getClient")
    fun getClient_whenExists(mockServer: MockServer) {
        val restTemplate = templateWithAuth(mockServer)
        val result: ClientOutput = fetchClient(restTemplate)
        assertThat(result.id).isEqualTo(TEST_CLIENT_OUT.id)
        assertThat(result.name).isEqualTo(TEST_CLIENT_OUT.name)
        assertThat(result.age).isEqualTo(TEST_CLIENT_OUT.age)
    }

    @Test
    @PactTestFor(pactMethod = "getClient_noAuth")
    fun getClient_whenNoAuth(mockServer: MockServer) {
        val restTemplate = templateWithoutAuth(mockServer)
        val e = assertThrows(HttpClientErrorException::class.java) { fetchClient(restTemplate) }
        assertThat(e.rawStatusCode).isEqualTo(401)
    }

    private fun fetchClient(restTemplate: RestTemplate): ClientOutput {
        return restTemplate.getForObject("/clients/{id}", ClientOutput::class.java, TEST_CLIENT_ID)
            ?: throw ClientNotFoundException(TEST_CLIENT_ID)
    }

    private fun templateWithAuth(mockServer: MockServer): RestTemplate {
        return RestTemplateBuilder()
            .rootUri(mockServer.getUrl())
            .defaultHeader(AUTHORIZATION, TEST_USER_TOKEN)
            .build()
    }

    private fun templateWithoutAuth(mockServer: MockServer): RestTemplate {
        return RestTemplateBuilder()
            .rootUri(mockServer.getUrl())
            .build()
    }

    companion object {
        const val BASIC_AUTH_REGEX = "Basic (?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?"
        const val URI_WITH_ID_REGEX = "^/clients/[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$"
        const val ISO_LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    }
}
