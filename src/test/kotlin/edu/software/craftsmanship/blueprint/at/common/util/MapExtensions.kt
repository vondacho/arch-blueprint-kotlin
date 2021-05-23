package edu.software.craftsmanship.blueprint.at.common.util

fun Map<String, String>.getOrFail(key: String): String =
    this[key] ?: throw IllegalArgumentException("DataTable creation error: missing $key")

fun Map<String, String>.getIntOrFail(key: String): Int = getOrFail(key).toInt()
fun Map<String, String>.getDoubleOrFail(key: String): Double = getOrFail(key).toDouble()

fun Map<String, String>.toPairs() = entries.map { it.key to it.value }
