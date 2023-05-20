package edu.obya.blueprint.client.at.config

import edu.obya.blueprint.client.domain.model.ClientId
import io.cucumber.java.ParameterType
import org.springframework.http.HttpStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CucumberTypeRegistryConfigurer {

    @ParameterType("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}")
    fun clientId(s: String): ClientId = ClientId.fromString(s)

    @ParameterType("\\d{4}.\\d{2}.\\d{2}T\\d{2}.\\d{2}.\\d{2}")
    fun timestamp(s: String): LocalDateTime = LocalDateTime.parse(s, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    @ParameterType("OK|CREATED|UNAUTHORIZED|FORBIDDEN|BAD_REQUEST|NOT_FOUND|NO_CONTENT")
    fun status(s: String) = HttpStatus.valueOf(s)
}
