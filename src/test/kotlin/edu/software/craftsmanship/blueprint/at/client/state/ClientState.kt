package edu.software.craftsmanship.blueprint.at.client.state

import edu.software.craftsmanship.blueprint.at.client.types.TestClient
import edu.software.craftsmanship.blueprint.at.common.util.bindNullable
import edu.software.craftsmanship.blueprint.domain.client.model.ClientId
import org.springframework.r2dbc.core.DatabaseClient
import java.time.LocalDateTime
import java.util.*

interface ClientState {
    fun setState(data: List<TestClient>)
    fun getState(): List<TestClient>
    fun reset()
}

class DatabaseClientState(private val dbClient: DatabaseClient) : ClientState {

    override fun setState(data: List<TestClient>) {
        data.onEach { insert(it) }
    }

    private fun insert(client: TestClient) {
        with(client) {
            dbClient.sql("""
                INSERT INTO client (id, name, age, created_at, modified_at)
                VALUES (:id, :name, :age, :created_at, :modified_at)
                """
            )
                .bind("id", UUID.fromString(id))
                .bind("name", name)
                .bindNullable("age", age, Integer::class)
                .bind("created_at", createdAt)
                .bind("modified_at", createdAt)
                .then()
                .block()
        }
    }

    override fun getState(): List<TestClient> =
        dbClient.sql("SELECT * FROM client WHERE activated")
            .map { row ->
                TestClient(
                    id = (row.get("id") as ClientId).toString(),
                    name = row.get("name") as String,
                    age = row.get("age") as Short?,
                    createdAt = row.get("created_at") as LocalDateTime,
                )
            }
            .all()
            .collectList()
            .block()
            ?.toList() ?: listOf()

    override fun reset() {
        dbClient.sql("DELETE FROM client").then().block()
    }
}
