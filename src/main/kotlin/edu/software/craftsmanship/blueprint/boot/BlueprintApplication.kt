package edu.software.craftsmanship.blueprint.boot

import edu.software.craftsmanship.blueprint.appl.config.ApplicationConfiguration
import edu.software.craftsmanship.blueprint.config.ExternalConfiguration
import edu.software.craftsmanship.blueprint.data.config.DataConfiguration
import edu.software.craftsmanship.blueprint.web.config.WebConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import java.time.ZoneOffset
import java.util.*

@SpringBootApplication(
    exclude = [
        JdbcTemplateAutoConfiguration::class,
        DataSourceTransactionManagerAutoConfiguration::class,
        DataSourceAutoConfiguration::class
    ]
)
@Import(
    value = [
        ApplicationConfiguration::class,
        WebConfiguration::class,
        DataConfiguration::class,
        ExternalConfiguration::class
    ]
)
class BlueprintApplication {
    init {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC))
    }
}

fun main(args: Array<String>) {
    runApplication<BlueprintApplication>(*args)
}
