package selapang.restful.tafakkur.com.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@Profile("test")
class TestSecurityConfig(
    private val mockUserFilter: MockJwtFilter
) {

    @Bean
    fun testSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf {
                it.disable()
            }
            .authorizeHttpRequests {
                it.anyRequest().permitAll() // Ijinkan semua request tanpa autentikasi
            }
            .sessionManagement{session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

        // Tidak ada filter autentikasi dalam mode pengujian
        http.addFilterBefore(mockUserFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}