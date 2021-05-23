package edu.software.craftsmanship.blueprint.at.client.types

import edu.software.craftsmanship.blueprint.web.model.ClientOutput

fun ClientOutput.toTestClient() =
    TestClient(
        id = this.id.toString(),
        name = this.name,
        age = this.age,
        createdAt = this.createdAt
    )

fun Array<ClientOutput>.toTestClientList() = this.map { it.toTestClient() }
