package edu.software.craftsmanship.blueprint.at.problemSolving.tortoiseRacing.steps

import edu.software.craftsmanship.blueprint.at.common.context.TestContext
import io.cucumber.java.en.Given
import io.cucumber.java8.En

class FixtureSteps: En {

    @Given("a vehicle A with speed of {int} foot per hour")
    fun aVehicleAWithSpeed(speed: Int) {
        TestContext.put("racing.speed.A", speed)
    }

    @Given("a vehicle B with speed of {int} foot per hour")
    fun aVehicleBWithSpeed(speed: Int) {
        TestContext.put("racing.speed.B", speed)
    }

    @Given("vehicle A is leading by {int} foot")
    fun vehicleAIsLeading(lead: Int) {
        TestContext.put("racing.lead", lead)
    }

    @Given("vehicle A is leading")
    fun vehicleAIsLeading() {
        vehicleAIsLeading(1)
    }

    @Given("a vehicle B is quicker than A by {int} foot per hour")
    fun vehicleBIsQuickerThanA(delta: Int) {
        val speedA: Int = TestContext.at("racing.speed.A")
        TestContext.put("racing.speed.B", speedA + delta)
    }
}
