package edu.software.craftsmanship.blueprint.config

import mu.KotlinLogging
import org.springframework.core.env.Environment

class RuntimeContext(private val environment: Environment) {
    private val log = KotlinLogging.logger {}

    val env: Env by lazy { inferEnvironment() }

    private fun inferEnvironment(): Env {
        val activeProfiles = environment.activeProfiles
        return when {
            "prod" in activeProfiles -> Env.PROD
            "dev" in activeProfiles -> Env.DEV
            else -> Env.TEST
        }.also {
            log.info { "Setting application environment to $it" }
        }
    }

    enum class Env {
        PROD, DEV, TEST
    }
}
