package edu.obya.blueprint.client.cdc

import edu.obya.blueprint.client.adapter.rest.ClientOutput
import edu.obya.blueprint.client.cdc.TestClient.TEST_CLIENT_ID
import edu.obya.blueprint.client.cdc.TestClient.TEST_CLIENT_STATE
import java.time.LocalDateTime

object TestClientOut {
    val TEST_CLIENT_OUT = ClientOutput(
        id = TEST_CLIENT_ID,
        name = TEST_CLIENT_STATE.name,
        age = TEST_CLIENT_STATE.age,
        createdAt = LocalDateTime.parse("2020-10-09T12:00:00")
    )
}
