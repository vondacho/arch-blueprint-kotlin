package edu.obya.blueprint.client.at.util

import org.springframework.r2dbc.core.DatabaseClient
import kotlin.reflect.KClass

fun DatabaseClient.GenericExecuteSpec.bindNullable(
    name: String,
    value: Any?,
    clazz: KClass<*>
): DatabaseClient.GenericExecuteSpec {
    return this.let {
        if (value != null) it.bind(name, value)
        else it.bindNull(name, clazz.java)
    }
}
