package edu.obya.blueprint.basic.at

import io.cucumber.junit.platform.engine.Constants
import io.cucumber.spring.CucumberContextConfiguration
import org.junit.platform.suite.api.ConfigurationParameter
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.test.context.SpringBootTest

@Suite
@SelectClasspathResource("features/basic")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "edu.obya.blueprint.basic.at")
class BasicAT

@CucumberContextConfiguration
@SpringBootTest
class BasicATBootstrap

@SpringBootConfiguration
class BasicATSpringBootConfiguration
