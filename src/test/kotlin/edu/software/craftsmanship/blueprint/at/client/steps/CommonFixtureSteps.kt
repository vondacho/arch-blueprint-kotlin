package edu.software.craftsmanship.blueprint.at.client.steps

import edu.software.craftsmanship.blueprint.at.common.context.Id
import edu.software.craftsmanship.blueprint.at.common.types.toDateTime
import io.cucumber.java.en.Given
import io.cucumber.java8.En
import org.mockito.Mockito
import java.time.LocalDateTime
import java.util.*
import java.util.function.Supplier

class CommonFixtureSteps(
    private val idSupplier: Supplier<UUID>,
    private val timestampSupplier: Supplier<LocalDateTime>
) : En {

    @Given("the next identifier is {word}")
    fun nextIdentifierIs(id: Id) {
        Mockito.`when`(idSupplier.get()).thenReturn(UUID.fromString(id))
    }

    @Given("the next timestamp is {word}")
    fun nextTimestampIs(timestamp: String) {
        Mockito.`when`(timestampSupplier.get()).thenReturn(timestamp.toDateTime())
    }
}
