package edu.software.craftsmanship.blueprint.at.common.context

typealias Id = String
typealias IdList = String

object TestContext {

    data class Element<T>(val id: Id, val instance: T)

    val store: MutableMap<Id, Element<Any>> = mutableMapOf()

    fun put(id: Id, instance: Any) = Element(
        id,
        instance
    ).also { store[id] = it }

    inline fun <reified T : Any> at(id: Id): T = atOpt(id)
        ?: throw IllegalArgumentException("no context object registered with $id")

    inline fun <reified T : Any> atOpt(id: Id): T? = store[id]?.let { it.instance as T }
    inline fun <reified T : Any> atOrDefault(id: Id, default: T): T = atOpt(id) ?: default

    fun reset() = store.clear()
}
