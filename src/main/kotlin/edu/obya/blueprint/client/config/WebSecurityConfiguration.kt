package edu.obya.blueprint.client.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource


@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
@Configuration
class WebSecurityConfiguration {

    @Bean
    fun securityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .cors(Customizer.withDefaults())
            .authorizeExchange()
            .pathMatchers("/clients", "/clients/**").hasRole("USER")
            .pathMatchers("/admin/shutdown").hasRole("ADMIN")
            .pathMatchers("/admin/**").permitAll()
            .anyExchange().authenticated()
            .and()
            .httpBasic()
            .and()
            .formLogin().disable()
            .csrf().disable()
            .build()
    }

    /**
     * @return a CORS configuration for trying out the API from Swagger UI
     */
    @Bean
    fun corsWebFilter(): CorsWebFilter {
        val configuration = CorsConfiguration()
        configuration.allowedOriginPatterns = listOf("http://localhost:*", "https://vondacho.github.io")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
        configuration.allowedHeaders = listOf(AUTHORIZATION, CONTENT_TYPE)
        configuration.maxAge = 3600L
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/clients/**", configuration)
        return CorsWebFilter(source)
    }

    @Profile("test", "local")
    @Bean
    fun userDetailsService(): ReactiveUserDetailsService {
       val passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
       return MapReactiveUserDetailsService(
           User.builder()
               .username("anonymous")
               .password(passwordEncoder.encode("none"))
               .roles("NONE")
               .build(),
           User.builder()
               .username("test")
               .password(passwordEncoder.encode("test"))
               .roles("USER")
               .build(),
           User.builder()
               .username("admin")
               .password(passwordEncoder.encode("admin"))
               .roles("USER", "ADMIN")
               .build()
       )
    }
}
