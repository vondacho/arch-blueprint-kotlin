package edu.obya.blueprint.problemsolving.at.steps.tortoiseRacing

import edu.obya.blueprint.common.at.context.TestContext.at
import edu.obya.blueprint.common.at.context.TestContext.put
import edu.obya.blueprint.problemsolving.domain.TortoiseRacingStrategy
import io.cucumber.java.en.When

class ActionSteps {

    @When("calculating the time needed by vehicle B to catch vehicle A")
    fun timeNeededByBToCatchA() {
        put("racing.result", TortoiseRacingStrategy().race(
            at("racing.speed.A"),
            at("racing.speed.B"),
            at("racing.lead")
        ))
    }
}
