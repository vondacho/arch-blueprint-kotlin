package edu.software.craftsmanship.blueprint.at.problemSolving.tortoiseRacing.steps

import edu.software.craftsmanship.blueprint.at.common.context.TestContext
import edu.software.craftsmanship.blueprint.domain.problemSolving.strategy.TortoiseRacingStrategy
import io.cucumber.java.en.When
import io.cucumber.java8.En

class ActionSteps: En {

    @When("calculating the time needed by vehicle B to catch vehicle A")
    fun timeNeededByBToCatchA() {
        TestContext.put("racing.result", TortoiseRacingStrategy().race(
            TestContext.at("racing.speed.A"),
            TestContext.at("racing.speed.B"),
            TestContext.at("racing.lead")
        ))
    }
}
