package edu.obya.blueprint.client.at.steps

import edu.obya.blueprint.common.at.context.TestContext.at
import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus

class WebAssertionSteps {

    @Then("the response status is {status}")
    fun responseStatusIs(status: HttpStatus) {
        assertThat(at("response.status") as HttpStatus).isEqualTo(status)
    }
}
