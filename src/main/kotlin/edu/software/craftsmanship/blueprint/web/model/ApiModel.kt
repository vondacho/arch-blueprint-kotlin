package edu.software.craftsmanship.blueprint.web.model

import com.fasterxml.jackson.annotation.JsonInclude
import edu.software.craftsmanship.blueprint.domain.client.model.Client
import edu.software.craftsmanship.blueprint.domain.client.model.ClientId
import java.time.LocalDateTime

interface Output

class EmptyOutput : Output

data class ListOutput<T>(val items: List<T>) : Output

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ClientOutput(
    val id: ClientId,
    val name: String,
    val age: Short?,
    val createdAt: LocalDateTime,
    val activated: Boolean = true
) : Output

fun Client.toOutput(): Output = ClientOutput(
    id = id,
    name = state.name,
    age = state.age,
    createdAt = state.createdAt!!,
    activated = state.activated
)
