package edu.software.craftsmanship.blueprint.at

import edu.software.craftsmanship.blueprint.appl.config.ApplicationConfiguration
import edu.software.craftsmanship.blueprint.at.client.config.ClientATConfiguration
import edu.software.craftsmanship.blueprint.at.client.state.ClientState
import edu.software.craftsmanship.blueprint.at.common.config.ATWebConfiguration
import edu.software.craftsmanship.blueprint.at.common.context.TestContext
import edu.software.craftsmanship.blueprint.config.ExternalConfiguration
import edu.software.craftsmanship.blueprint.data.config.DataConfiguration
import edu.software.craftsmanship.blueprint.doc.appmap.ScenarioRecorder
import edu.software.craftsmanship.blueprint.web.config.WebConfiguration
import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.Scenario
import io.cucumber.spring.CucumberContextConfiguration
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
@Import(
    value = [
        ATWebConfiguration::class,
        ClientATConfiguration::class
    ]
)
@CucumberContextConfiguration
class AllFeaturesATConfiguration {

    @Autowired
    lateinit var clientState: ClientState

    @Before
    fun startScenarioRecording(scenario: Scenario) {
        ScenarioRecorder.start(scenario)
    }

    @After
    fun stopScenarioRecording() {
        ScenarioRecorder.stop()
    }

    @After
    fun resetMocks() {
    }

    @After
    fun resetTestDatabase() {
        clientState.reset()
    }

    @After
    fun resetTestContext() {
        TestContext.reset()
    }
}
@ComponentScan(basePackageClasses = [
    ApplicationConfiguration::class,
    WebConfiguration::class,
    DataConfiguration::class,
    ExternalConfiguration::class
])

@SpringBootConfiguration
class AllFeaturesATSpringBootConfiguration
