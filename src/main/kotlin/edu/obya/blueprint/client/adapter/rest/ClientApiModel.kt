package edu.obya.blueprint.client.adapter.rest

import com.fasterxml.jackson.annotation.JsonInclude
import edu.obya.blueprint.client.domain.model.Client
import edu.obya.blueprint.client.domain.model.ClientId
import java.time.LocalDateTime

interface Output

class EmptyOutput : Output

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ClientOutput(
    val id: ClientId,
    val name: String,
    val age: Int?,
    val createdAt: LocalDateTime,
    val activated: Boolean = true
) : Output

fun Client.toOutput() = ClientOutput(
    id = id,
    name = state.name,
    age = state.age,
    createdAt = state.createdAt!!,
    activated = state.activated
)
