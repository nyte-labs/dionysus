package com.nytelabs.dionysus.controller

import com.nytelabs.dionysus.persistence.AccountRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(val accountRepository: AccountRepository) {
    @Bean
    fun userDetailsService(): UserDetailsService = UserDetailsService { username ->
        val user = accountRepository.findByUsername(username)
        if (user == null || !user.enabled) {
            throw UsernameNotFoundException("User not found: $username")
        }

        User(user.username, user.password, listOf())
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain = http
        .authorizeHttpRequests { authorize ->
            authorize.requestMatchers("/login", "/css/**", "/fonts/**", "/favicon.ico", "/img/**").permitAll()
                .anyRequest().authenticated()
        }
        .formLogin { form ->
            form.loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/", true)
        }
        .sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
        }
        .exceptionHandling { exceptions ->
            exceptions.accessDeniedHandler(SecurityAccessDeniedHandler())
        }
        .logout { logout ->
            logout.permitAll()
        }
        .build()

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager =
        authenticationConfiguration.authenticationManager

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider(passwordEncoder: PasswordEncoder, userDetailsService: UserDetailsService) =
        DaoAuthenticationProvider(userDetailsService).apply {
            setPasswordEncoder(passwordEncoder)
        }
}
