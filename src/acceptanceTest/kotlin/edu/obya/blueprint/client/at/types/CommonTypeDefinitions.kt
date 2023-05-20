package edu.obya.blueprint.client.at.types

interface TestData<T> {
    val id: T
}
data class TestResult<T, S>(val content: T? = null, val status: S)
