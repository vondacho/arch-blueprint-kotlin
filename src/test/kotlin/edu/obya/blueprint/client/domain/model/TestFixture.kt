package edu.obya.blueprint.client.domain.model

import java.time.LocalDateTime
import java.util.*

fun newClientId(id: String? = null): ClientId = id?.let { UUID.fromString(it) } ?: randomUUID()

private fun randomUUID() = UUID.randomUUID()

/**
 * Mirrored by insert-data.sql script
 */
object TestClients {
    val client1 = Client(
        id = newClientId("ce751f30-217a-422c-b81b-8f75df4917b6"),
        state = Client.State(
            name = "test",
            age = 21,
            createdAt = LocalDateTime.parse("2020-10-10T12:00:00")
        )
    )
    val client2 = Client(
        id = newClientId("8be97d10-5efc-40d6-92ba-95431dc7fe6a"),
        state = Client.State(
            name = "x",
            age = 22,
            createdAt = LocalDateTime.parse("2020-10-10T12:00:00")
        )
    )
    val client3 = Client(
        id = newClientId("9ac9ff7f-4a0a-4a15-9faa-5682c8a62de3"),
        state = Client.State(
            name = "deactivated",
            age = 23,
            createdAt = LocalDateTime.parse("2020-10-10T12:00:00"),
            activated = false
        )
    )
}
