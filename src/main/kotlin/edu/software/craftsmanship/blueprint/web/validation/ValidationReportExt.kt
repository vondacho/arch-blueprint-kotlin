package edu.software.craftsmanship.blueprint.web.validation

import com.atlassian.oai.validator.report.ValidationReport

fun ValidationReport.asText() = this.allFinalMessages().asText()

fun ValidationReport.allFinalMessages(): List<ValidationReport.Message> {
    fun all(current: List<ValidationReport.Message>): List<ValidationReport.Message> =
        if (current.isNotEmpty()) {
            val messages = mutableListOf<ValidationReport.Message>()
            current.onEach {
                if (it.nestedMessages.isNullOrEmpty()) {
                    messages.add(it)
                } else {
                    messages.addAll(all(it.nestedMessages))
                }
            }
            messages
        } else {
            current
        }
    return all(messages)
}

fun List<ValidationReport.Message>.asText() = this
    .map { it.message }
    .distinct()
    .joinToString(separator = ", ")

fun List<ValidationReport.Message>.errors() = this.filter { it.level == ValidationReport.Level.ERROR }
fun List<ValidationReport.Message>.hasKey(key: String) = this.any { it.key == key }

object ValidationReportKeys {
    const val missingPath = "validation.request.path.missing"
    const val notAllowedContentType = "validation.request.contentType.notAllowed"
    const val invalidJsonPayload = "validation.request.body.schema.invalidJson"
    const val processingError = "validation.request.body.schema.processingError"
}
