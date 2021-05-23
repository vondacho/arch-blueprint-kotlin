package edu.software.craftsmanship.blueprint.at.client.types

import com.fasterxml.jackson.annotation.JsonInclude
import edu.software.craftsmanship.blueprint.at.common.context.Id
import edu.software.craftsmanship.blueprint.at.common.types.TestData
import edu.software.craftsmanship.blueprint.at.common.types.toDateTime
import edu.software.craftsmanship.blueprint.at.common.types.toNullable
import edu.software.craftsmanship.blueprint.at.common.util.getIntOrFail
import edu.software.craftsmanship.blueprint.at.common.util.getOrFail
import io.cucumber.java.DataTableType
import java.time.LocalDateTime

data class TestClient(
    override val id: Id,
    val name: String,
    val age: Short?,
    val createdAt: LocalDateTime,
) : TestData

@DataTableType
fun existingClientEntry(entry: Map<String, String>): Pair<Int, TestClient> =
    entry.getIntOrFail("model-id") to existingClientAttributesEntry(entry)

@DataTableType
fun existingClientAttributesEntry(entry: Map<String, String>): TestClient =
    TestClient(
        id = entry.getOrFail("id"),
        name = entry.getOrFail("name"),
        age = entry["age"]?.toNullable()?.toShort(),
        createdAt = entry.getOrFail("creation-date").toDateTime()
    )

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TestClientInput(
    val name: String?,
    val age: Short?
)

@DataTableType
fun clientInputEntry(entry: Map<String, String>): TestClientInput =
    TestClientInput(
        name = entry["name"]?.toNullable(),
        age = entry["age"]?.toNullable()?.toShort()
    )
