package edu.software.craftsmanship.blueprint.domain.client.model

import java.time.LocalDateTime
import java.util.*

typealias ClientId = UUID

data class Client(
    val id: ClientId,
    val state: State
) {
    data class State(
        val name: String,
        val age: Short? = null,
        val createdAt: LocalDateTime? = null,
        val activated: Boolean = true
    )

    fun activate(yes: Boolean = true) = this.copy(state = this.state.copy(activated = yes))
}
