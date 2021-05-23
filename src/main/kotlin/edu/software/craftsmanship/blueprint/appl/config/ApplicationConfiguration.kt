package edu.software.craftsmanship.blueprint.appl.config

import edu.software.craftsmanship.blueprint.appl.usecase.ClientUseCase
import edu.software.craftsmanship.blueprint.domain.client.repository.ClientRepository
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@EnableTransactionManagement
@Configuration
class ApplicationConfiguration {

    @Bean
    fun transactionManager(connectionFactory: ConnectionFactory): ReactiveTransactionManager {
        return R2dbcTransactionManager(connectionFactory)
    }

    @Bean
    fun clientUseCase(clientRepository: ClientRepository) = ClientUseCase(clientRepository)
}
