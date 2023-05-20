package edu.obya.blueprint.client.cdc

import edu.obya.blueprint.client.domain.model.Client
import edu.obya.blueprint.client.domain.model.ClientId
import java.time.LocalDateTime

object TestClient {
    val TEST_CLIENT_ID: ClientId = ClientId.fromString("64a0f7d1-7b25-412d-b1e0-abacde3c21cd")
    
    val TEST_CLIENT_STATE = Client.State(
        name = "John", 
        age = 21, 
        createdAt = LocalDateTime.parse("2020-10-09T12:00:00")
    )
    
    val TEST_CLIENT = Client(id = TEST_CLIENT_ID, state = TEST_CLIENT_STATE)
}
