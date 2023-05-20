package edu.obya.blueprint.client.at

import edu.obya.blueprint.client.adapter.r2dbc.ClientR2dbcConfiguration
import edu.obya.blueprint.client.adapter.rest.ClientController
import edu.obya.blueprint.client.application.ClientApplicationConfiguration
import edu.obya.blueprint.client.at.config.ATWebConfiguration
import edu.obya.blueprint.client.at.config.ClientATConfiguration
import edu.obya.blueprint.client.at.config.PostgresR2dbcDataSourceTestConfiguration
import edu.obya.blueprint.client.at.state.ClientState
import edu.obya.blueprint.client.config.ExceptionHandling
import edu.obya.blueprint.client.config.WebSecurityConfiguration
import edu.obya.blueprint.client.config.WebValidationConfiguration
import edu.obya.blueprint.common.at.context.TestContext
import io.cucumber.java.After
import io.cucumber.junit.platform.engine.Constants
import io.cucumber.spring.CucumberContextConfiguration
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.junit.platform.suite.api.ConfigurationParameter
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@Suite
@SelectClasspathResource("features/client")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "edu.obya.blueprint.client.at")
internal class ClientAT

@ActiveProfiles("test", "r2dbc", "at")
@AutoConfigureEmbeddedDatabase
@EnableAutoConfiguration
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
internal class ClientATBootstrap {

    @Autowired
    lateinit var clientState: ClientState

    @After
    fun resetTestDatabase() {
        clientState.reset()
    }

    @After
    fun resetTestContext() {
        TestContext.reset()
    }

    @Import(value = [
        ATWebConfiguration::class,
        ClientATConfiguration::class,
        ClientController::class,
        WebSecurityConfiguration::class,
        WebValidationConfiguration::class,
        ExceptionHandling::class,
        ClientApplicationConfiguration::class,
        ClientR2dbcConfiguration::class,
        PostgresR2dbcDataSourceTestConfiguration::class
    ])
    @TestConfiguration
    internal class TestConfig
}

@SpringBootConfiguration
internal class ClientATSpringBootConfiguration