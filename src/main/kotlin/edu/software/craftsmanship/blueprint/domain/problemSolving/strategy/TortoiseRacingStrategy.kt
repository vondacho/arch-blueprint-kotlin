package edu.software.craftsmanship.blueprint.domain.problemSolving.strategy

/**
 * See https://bitbucket.org/socraagile/tortoise_racing_car/src/elixir-olivier/
 */
class TortoiseRacingStrategy {

    data class CatchingTime(val hours: Int, val minutes: Int, val seconds: Int)

    fun race(speedA: Int, speedB: Int, lead: Int): CatchingTime =
        if (speedA >= speedB) undefinedTime
        else {
            val hours = lead.div(speedB - speedA) to lead.mod(speedB - speedA)
            val minutes = (hours.second * 60).div(speedB - speedA) to (hours.second * 60).mod(speedB - speedA)
            val seconds = (minutes.second * 60).div(speedB - speedA)
            CatchingTime(hours.first, minutes.first, seconds)
        }

    companion object {
        val undefinedTime = CatchingTime(-1, -1, -1)
    }
}
