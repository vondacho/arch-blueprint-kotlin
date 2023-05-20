package edu.obya.blueprint.problemsolving.at.steps.tortoiseRacing

import edu.obya.blueprint.common.at.context.TestContext.at
import edu.obya.blueprint.common.at.context.TestContext.put
import io.cucumber.java.en.Given

class FixtureSteps {

    @Given("a vehicle A with speed of {int} foot per hour")
    fun aVehicleAWithSpeed(speed: Int) {
        put("racing.speed.A", speed)
    }

    @Given("a vehicle B with speed of {int} foot per hour")
    fun aVehicleBWithSpeed(speed: Int) {
        put("racing.speed.B", speed)
    }

    @Given("vehicle A is leading by {int} foot")
    fun vehicleAIsLeading(lead: Int) {
        put("racing.lead", lead)
    }

    @Given("vehicle A is leading")
    fun vehicleAIsLeading() {
        vehicleAIsLeading(1)
    }

    @Given("a vehicle B is quicker than A by {int} foot per hour")
    fun vehicleBIsQuickerThanA(delta: Int) {
        val speedA: Int = at("racing.speed.A")
        put("racing.speed.B", speedA + delta)
    }
}
