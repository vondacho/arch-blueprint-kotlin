package edu.obya.blueprint.problemsolving.at

import io.cucumber.junit.platform.engine.Constants
import io.cucumber.spring.CucumberContextConfiguration
import org.junit.platform.suite.api.ConfigurationParameter
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.test.context.SpringBootTest

@Suite
@SelectClasspathResource("features/problemsolving")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "edu.obya.blueprint.problemsolving.at")
internal class ProblemSolvingAT

@CucumberContextConfiguration
@SpringBootTest
internal class ProblemSolvingATBootstrap

@SpringBootConfiguration
internal class ProblemSolvingATSpringBootConfiguration
