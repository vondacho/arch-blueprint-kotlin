package edu.obya.blueprint.client.at.config

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.postgresql.api.PostgresqlConnection
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryMetadata
import org.postgresql.ds.common.BaseDataSource
import org.reactivestreams.Publisher
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import javax.sql.DataSource

@TestConfiguration
class PostgresR2dbcDataSourceTestConfiguration {

    @Bean
    fun connectionFactory(datasource: DataSource): ConnectionFactory {
        return EmbeddedJdbcToR2dbcPostgresAdapter(datasource)
    }
}

/**
 * An adapter between JDBC embedded postgres and R2DBC postgres.
 * The R2DBC connection factory has to be aligned at runtime on the configuration of the running embedded JDBC data source.
 */
internal class EmbeddedJdbcToR2dbcPostgresAdapter(private val jdbcDataSource: DataSource) : ConnectionFactory {

    private var jdbcDataSourceConfig: BaseDataSource? = null
    private var r2dbcConnectionFactory: PostgresqlConnectionFactory? = null

    private fun connectionFactory(): PostgresqlConnectionFactory {
        return run {
            val freshJdbcDataSourceConfig = jdbcDataSource.unwrap(BaseDataSource::class.java)

            if (r2dbcConnectionFactory == null || jdbcDataSourceConfig != freshJdbcDataSourceConfig) {
                jdbcDataSourceConfig = freshJdbcDataSourceConfig
                with(freshJdbcDataSourceConfig) {
                    r2dbcConnectionFactory = PostgresqlConnectionFactory(
                        PostgresqlConnectionConfiguration.builder()
                            .host(this.serverNames.first())
                            .port(this.portNumbers.first())
                            .username(this.user ?: throw IllegalStateException("No user defined on the jdbc datasource."))
                            .password(this.password)
                            .database(this.databaseName)
                            .build()
                    )
                }
            }
            r2dbcConnectionFactory ?: throw IllegalStateException(
                "R2dbc connection factory could not be initialized based on the jdbc datasource.")
        }
    }

    override fun create(): Publisher<PostgresqlConnection> = connectionFactory().create()

    override fun getMetadata(): ConnectionFactoryMetadata = connectionFactory().metadata
}
