package edu.software.craftsmanship.blueprint.data.config

import edu.software.craftsmanship.blueprint.data.adapter.ClientRepositoryAdapter
import edu.software.craftsmanship.blueprint.data.adapter.r2dbc.R2dbcClientRepository
import edu.software.craftsmanship.blueprint.data.model.ClientData
import edu.software.craftsmanship.blueprint.domain.client.model.ClientId
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import java.time.LocalDateTime
import java.util.*
import java.util.function.Supplier

@EntityScan(basePackageClasses = [ClientData::class])
@EnableR2dbcRepositories(basePackageClasses = [R2dbcClientRepository::class])
@Configuration
class DataConfiguration {

    @Bean
    fun idSupplier(): Supplier<UUID> = Supplier<UUID> { UUID.randomUUID() }

    @Bean
    fun timestampSupplier(): Supplier<LocalDateTime> = Supplier<LocalDateTime> { LocalDateTime.now() }

    @Bean
    fun clientRepository(
        repository: R2dbcClientRepository,
        idSupplier: Supplier<ClientId>,
        timestampSupplier: Supplier<LocalDateTime>
    ) = ClientRepositoryAdapter(repository, idSupplier, timestampSupplier)
}
