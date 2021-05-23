package edu.software.craftsmanship.blueprint.at.problemSolving.mumbling.steps

import edu.software.craftsmanship.blueprint.at.common.context.TestContext
import edu.software.craftsmanship.blueprint.domain.problemSolving.strategy.MumblingStrategy
import io.cucumber.java.en.When
import io.cucumber.java8.En

class ActionSteps: En {

    @When("mumbling {word}")
    fun mumble(text: String) {
        TestContext.put("mumbling.result", MumblingStrategy().mumble(text))
    }
}
