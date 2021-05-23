package edu.software.craftsmanship.blueprint.at.common.steps

import edu.software.craftsmanship.blueprint.at.common.context.TestContext.at
import edu.software.craftsmanship.blueprint.at.common.types.toHttpStatus
import io.cucumber.java.en.Then
import io.cucumber.java8.En
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus

class WebAssertionSteps : En {

    @Then("the response status is {word}")
    fun responseStatusIs(status: String) {
        assertThat(at("response.status") as HttpStatus).isEqualTo(status.toHttpStatus())
    }
}
