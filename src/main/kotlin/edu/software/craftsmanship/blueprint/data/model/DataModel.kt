package edu.software.craftsmanship.blueprint.data.model

import edu.software.craftsmanship.blueprint.domain.client.model.ClientId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceConstructor
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("client")
data class ClientData(
    @Id
    @Column("id")
    private val _id: ClientId,
    val name: String,
    val age: Short?,
    val createdAt: LocalDateTime,
    val activated: Boolean = true,
    private val modifiedAt: LocalDateTime,
    @Transient
    private var isNewInstance: Boolean = false
) : Persistable<ClientId> {

    @PersistenceConstructor
    constructor(
        _id: ClientId,
        name: String,
        age: Short?,
        createdAt: LocalDateTime,
        modifiedAt: LocalDateTime,
        activated: Boolean
    ) : this(_id, name, age, createdAt, activated, modifiedAt, false)

    override fun isNew(): Boolean = isNewInstance
    override fun getId(): ClientId = _id
}
