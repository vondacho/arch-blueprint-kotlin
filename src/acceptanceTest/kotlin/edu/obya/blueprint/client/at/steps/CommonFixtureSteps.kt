package edu.obya.blueprint.client.at.steps

import edu.obya.blueprint.client.domain.model.ClientId
import io.cucumber.java.en.Given
import org.mockito.Mockito
import java.time.LocalDateTime
import java.util.*
import java.util.function.Supplier

class CommonFixtureSteps(
    private val idSupplier: Supplier<UUID>,
    private val timestampSupplier: Supplier<LocalDateTime>
) {

    @Given("the next identifier is {clientId}")
    fun nextIdentifierIs(id: ClientId) {
        Mockito.`when`(idSupplier.get()).thenReturn(id)
    }

    @Given("the next timestamp is {timestamp}")
    fun nextTimestampIs(timestamp: LocalDateTime) {
        Mockito.`when`(timestampSupplier.get()).thenReturn(timestamp)
    }
}
