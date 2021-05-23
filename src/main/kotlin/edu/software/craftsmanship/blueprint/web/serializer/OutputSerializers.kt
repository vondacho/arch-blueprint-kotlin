package edu.software.craftsmanship.blueprint.web.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import edu.software.craftsmanship.blueprint.web.model.ListOutput
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class ListOutputSerializer<Any> : JsonSerializer<ListOutput<Any>>() {

    override fun serialize(
        list: ListOutput<Any>,
        jsonGenerator: JsonGenerator,
        serializerProvider: SerializerProvider
    ) {
        jsonGenerator.writeStartArray()
        list.items.onEach { jsonGenerator.writeObject(it) }
        jsonGenerator.writeEndArray()
    }
}
