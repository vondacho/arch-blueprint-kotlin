package edu.software.craftsmanship.blueprint.at.common.types

import edu.software.craftsmanship.blueprint.at.common.context.Id

interface TestData {
    val id: Id
}
data class TestResult<T, S>(val content: T? = null, val status: S)
