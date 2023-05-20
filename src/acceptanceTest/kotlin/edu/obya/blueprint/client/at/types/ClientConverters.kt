package edu.obya.blueprint.client.at.types

import edu.obya.blueprint.client.adapter.rest.ClientOutput

fun ClientOutput.toTestClient() =
    TestClient(
        id = this.id,
        name = this.name,
        age = this.age,
        createdAt = this.createdAt
    )

fun Array<ClientOutput>.toTestClientList() = this.map { it.toTestClient() }
