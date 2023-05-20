package edu.obya.blueprint.client.cdc

import edu.obya.blueprint.client.application.ClientInput
import edu.obya.blueprint.client.cdc.TestClient.TEST_CLIENT_STATE

object TestClientIn {
    val TEST_CLIENT_IN = ClientInput(
        name = TEST_CLIENT_STATE.name,
        age = TEST_CLIENT_STATE.age
    )
}
