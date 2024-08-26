package blog.restful.tafakkur.com.config

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import blog.restful.tafakkur.com.dto.FormatResponse
import blog.restful.tafakkur.com.exception.UnauthorizedException
import blog.restful.tafakkur.com.service.TokenBlacklistService
import org.springframework.util.AntPathMatcher

@Component
class JwtFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService,
    private val objectMapper: ObjectMapper,
    private val tokenBlacklistService: TokenBlacklistService
) : OncePerRequestFilter() {
    private val pathMatcher = AntPathMatcher()
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val requestPath = request.servletPath
            if (isExcludedPath(requestPath)) {
                filterChain.doFilter(request, response)
                return
            }

            val authorizationHeader = request.getHeader("Authorization")
            var username: String? = null
            var jwt: String? = null

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7)
                if (tokenBlacklistService.isTokenBlacklisted(jwt)) {
                    sendErrorResponse(response, "Token has been logged out")
                    return
                }
                username = jwtUtil.extractUsername(jwt)
            }

            if (jwt == null) {
                throw UnauthorizedException("Unauthorized")
            }

            if (SecurityContextHolder.getContext().authentication == null) {
                val userDetails = userDetailsService.loadUserByUsername(username)
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    val authToken = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                } else {
                    sendErrorResponse(response, "Invalid JWT token")
                }
            }

            filterChain.doFilter(request, response)
        } catch (ex: Exception) {
            sendErrorResponse(response, "Unauthorized")
        }

    }

    private fun isExcludedPath(requestPath: String): Boolean {
        // Daftar path yang dikecualikan dengan wildcard
        val excludedPaths = listOf(
            "/api/auth/login",
            "/api/auth/register",
            "/v3/api-docs/**",
            "/swagger-ui/**"
        )
        return excludedPaths.any { pathPattern -> pathMatcher.match(pathPattern, requestPath) }
    }


    private fun sendErrorResponse(response: HttpServletResponse, message: String) {
        val errorResponse = FormatResponse.Error(
            code = 401,
            message = message,
            data = null
        )

        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }

}