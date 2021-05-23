package edu.software.craftsmanship.blueprint.at.common.types

import edu.software.craftsmanship.blueprint.at.common.context.Id
import edu.software.craftsmanship.blueprint.at.common.context.IdList
import org.springframework.http.HttpStatus
import java.time.LocalDateTime
import java.time.LocalDateTime.now

fun String.toNullable(): String? = this.takeUnless { it == "-" }

fun String.toDateTimeOrNull(): LocalDateTime? = toNullable()
    ?.let { if (it == "now") now() else it.toDateTime() }

fun String.toDateTime(): LocalDateTime = LocalDateTime.parse(this)

fun String.toHttpStatus(): HttpStatus = HttpStatus.valueOf(this)

fun IdList.toIdList(): List<Id> = toNullable()?.split(",") ?: emptyList()

fun IdList.toIntIdList() = toIdList().map { it.toInt() }

fun String.toKeyList(): List<String> = toNullable()?.split(",") ?: emptyList()

fun String.toKeyValueList(): List<Pair<String, String>> = toNullable()
    ?.split(",")
    ?.filter { it.isNotEmpty() }
    ?.mapNotNull {
        it.split(":")
            .takeIf { elt -> elt.size == 2 }
            ?.let { (key, value) -> key to value }
    }
    ?: emptyList()
