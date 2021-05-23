package edu.software.craftsmanship.blueprint.at.problemSolving.tortoiseRacing.steps

import edu.software.craftsmanship.blueprint.at.common.context.TestContext
import edu.software.craftsmanship.blueprint.domain.problemSolving.strategy.TortoiseRacingStrategy
import io.cucumber.java.en.Then
import io.cucumber.java8.En
import org.assertj.core.api.Assertions.assertThat

class AssertionSteps: En {

    @Then("the resulting time is {int} hour.s")
    fun resultingTimeInHoursIs(hours: Int) {
        val result: TortoiseRacingStrategy.CatchingTime = TestContext.at("racing.result")
        assertThat(result.hours).isEqualTo(hours)
    }

    @Then("the resulting time is {int} minute.s")
    fun resultingTimeInMinutesIs(minutes: Int) {
        val result: TortoiseRacingStrategy.CatchingTime = TestContext.at("racing.result")
        assertThat(result.minutes).isEqualTo(minutes)
    }

    @Then("the resulting time is {int} second.s")
    fun resultingTimeInSecondsIs(seconds: Int) {
        val result: TortoiseRacingStrategy.CatchingTime = TestContext.at("racing.result")
        assertThat(result.seconds).isEqualTo(seconds)
    }

    @Then("the resulting time is {int} hour.s {int} minute.s {int} second.s")
    fun resultingTimeIs(hours: Int, minutes: Int, seconds: Int) {
        val result: TortoiseRacingStrategy.CatchingTime = TestContext.at("racing.result")
        assertThat(result).isEqualTo(TortoiseRacingStrategy.CatchingTime(hours, minutes, seconds))
    }

    @Then("the resulting time is undefined")
    fun resultingTimeIsUndefined() {
        val result: TortoiseRacingStrategy.CatchingTime = TestContext.at("racing.result")
        assertThat(result).isEqualTo(TortoiseRacingStrategy.undefinedTime)
    }
}
