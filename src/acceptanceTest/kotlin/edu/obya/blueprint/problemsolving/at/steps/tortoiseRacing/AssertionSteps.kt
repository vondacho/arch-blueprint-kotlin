package edu.obya.blueprint.problemsolving.at.steps.tortoiseRacing

import edu.obya.blueprint.common.at.context.TestContext.at
import edu.obya.blueprint.problemsolving.domain.TortoiseRacingStrategy
import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions.assertThat

class AssertionSteps {

    @Then("the resulting time is {int} hour.s")
    fun resultingTimeInHoursIs(hours: Int) {
        val result: TortoiseRacingStrategy.CatchingTime = at("racing.result")
        assertThat(result.hours).isEqualTo(hours)
    }

    @Then("the resulting time is {int} minute.s")
    fun resultingTimeInMinutesIs(minutes: Int) {
        val result: TortoiseRacingStrategy.CatchingTime = at("racing.result")
        assertThat(result.minutes).isEqualTo(minutes)
    }

    @Then("the resulting time is {int} second.s")
    fun resultingTimeInSecondsIs(seconds: Int) {
        val result: TortoiseRacingStrategy.CatchingTime = at("racing.result")
        assertThat(result.seconds).isEqualTo(seconds)
    }

    @Then("the resulting time is {int} hour.s {int} minute.s {int} second.s")
    fun resultingTimeIs(hours: Int, minutes: Int, seconds: Int) {
        val result: TortoiseRacingStrategy.CatchingTime = at("racing.result")
        assertThat(result).isEqualTo(TortoiseRacingStrategy.CatchingTime(hours, minutes, seconds))
    }

    @Then("the resulting time is undefined")
    fun resultingTimeIsUndefined() {
        val result: TortoiseRacingStrategy.CatchingTime = at("racing.result")
        assertThat(result).isEqualTo(TortoiseRacingStrategy.undefinedTime)
    }
}
