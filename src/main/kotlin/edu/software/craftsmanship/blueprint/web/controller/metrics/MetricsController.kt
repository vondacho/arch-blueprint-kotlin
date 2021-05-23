package edu.software.craftsmanship.blueprint.web.controller.metrics

import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusScrapeEndpoint
import org.springframework.boot.actuate.metrics.export.prometheus.TextOutputFormat
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MetricsController(
    private val prometheusScrapeEndpoint: PrometheusScrapeEndpoint?
) {
    @GetMapping("metrics", produces = ["text/plain; version=0.0.4; charset=utf-8"])
    fun metrics(
        @RequestParam("includedNames", defaultValue = "") includedNames: Set<String> = emptySet()
    ): ResponseEntity<String> =
        prometheusScrapeEndpoint
            ?.let { ResponseEntity.ok(it.scrape(TextOutputFormat.CONTENT_TYPE_OPENMETRICS_100, includedNames).body) }
            ?: notFound().build()
}
