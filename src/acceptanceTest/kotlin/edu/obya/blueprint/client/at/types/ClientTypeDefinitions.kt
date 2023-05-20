package edu.obya.blueprint.client.at.types

import com.fasterxml.jackson.annotation.JsonInclude
import edu.obya.blueprint.client.at.util.getIntOrFail
import edu.obya.blueprint.client.at.util.getOrFail
import edu.obya.blueprint.client.domain.model.ClientId
import io.cucumber.java.DataTableType
import java.time.LocalDateTime

data class TestClient(
    override val id: ClientId,
    val name: String,
    val age: Int?,
    val createdAt: LocalDateTime,
) : TestData<ClientId>

@DataTableType
fun existingClientEntry(entry: Map<String, String>): Pair<Int, TestClient> =
    entry.getIntOrFail("model-id") to existingClientAttributesEntry(entry)

@DataTableType
fun existingClientAttributesEntry(entry: Map<String, String>): TestClient =
    TestClient(
        id = ClientId.fromString(entry.getOrFail("id")) ,
        name = entry.getOrFail("name"),
        age = entry["age"]?.toNullable()?.toInt(),
        createdAt = entry.getOrFail("creation-date").toDateTime()
    )

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TestClientInput(
    val name: String?,
    val age: Int?
)

@DataTableType
fun clientInputEntry(entry: Map<String, String>): TestClientInput =
    TestClientInput(
        name = entry["name"]?.toNullable(),
        age = entry["age"]?.toNullable()?.toInt()
    )
