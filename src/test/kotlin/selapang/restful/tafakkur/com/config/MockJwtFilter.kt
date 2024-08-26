package selapang.restful.tafakkur.com.config

import jakarta.servlet.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component

@Component
class MockJwtFilter: Filter {
    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig?) {
        // Inisialisasi jika diperlukan
    }


    @Throws(ServletException::class, java.io.IOException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        // Buat pengguna tiruan dengan username dan roles yang diinginkan
        val mockUser = User(
            "admin",
            "",
            listOf(SimpleGrantedAuthority("ROLE_USER"))
        )

        val authentication = UsernamePasswordAuthenticationToken(mockUser, null, mockUser.authorities)
        SecurityContextHolder.getContext().authentication = authentication

        // Lanjutkan ke filter berikutnya
        chain.doFilter(request, response)
    }

    override fun destroy() {
        // Clean up jika diperlukan
    }
}