package edu.obya.blueprint.client.adapter.r2dbc

import edu.obya.blueprint.client.domain.model.ClientId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceCreator
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
    val age: Int?,
    val createdAt: LocalDateTime,
    val activated: Boolean = true,
    private val modifiedAt: LocalDateTime,
    @Transient
    private var isNewInstance: Boolean = false
) : Persistable<ClientId> {

    @PersistenceCreator
    constructor(
        _id: ClientId,
        name: String,
        age: Int?,
        createdAt: LocalDateTime,
        modifiedAt: LocalDateTime,
        activated: Boolean
    ) : this(_id, name, age, createdAt, activated, modifiedAt, false)

    override fun isNew(): Boolean = isNewInstance
    override fun getId(): ClientId = _id
}
