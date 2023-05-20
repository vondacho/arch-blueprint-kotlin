package edu.obya.blueprint.client.adapter.r2dbc

import edu.obya.blueprint.client.domain.model.ClientId
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import java.time.LocalDateTime
import java.util.*
import java.util.function.Supplier

@Profile("r2dbc")
@EntityScan(basePackageClasses = [ClientData::class])
@EnableR2dbcRepositories(basePackageClasses = [R2dbcClientRepository::class])
@Configuration
class ClientR2dbcConfiguration {

    @Bean
    fun idSupplier(): Supplier<UUID> = Supplier<UUID> { UUID.randomUUID() }

    @Bean
    fun timestampSupplier(): Supplier<LocalDateTime> = Supplier<LocalDateTime> { LocalDateTime.now() }

    @Bean
    fun clientRepository(
        repository: R2dbcClientRepository,
        idSupplier: Supplier<ClientId>,
        timestampSupplier: Supplier<LocalDateTime>
    ) = ClientRepositoryR2dbcAdapter(repository, idSupplier, timestampSupplier)
}
