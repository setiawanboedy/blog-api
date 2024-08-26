package blog.restful.tafakkur.com.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.validation.annotation.Validated
import blog.restful.tafakkur.com.config.JwtUtil
import blog.restful.tafakkur.com.service.TokenBlacklistService
import blog.restful.tafakkur.com.dto.request.RegisterRequest
import blog.restful.tafakkur.com.dto.request.AuthenticationRequest
import blog.restful.tafakkur.com.dto.response.AuthenticationResponse
import blog.restful.tafakkur.com.dto.FormatResponse
import blog.restful.tafakkur.com.exception.BadCredentialsException
import blog.restful.tafakkur.com.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@Validated
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService,
    private val userService: UserService,
    private val tokenBlacklist: TokenBlacklistService,
) {

    @Operation(summary = "Login to app", description = "Authenticate to get jwt token")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful login"),
            ApiResponse(responseCode = "401", description = "Bad credentials")
        ]
    )
    @PostMapping(
        value = ["/login"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun createAuthenticationToken(@Valid @RequestBody authenticationRequest: AuthenticationRequest): FormatResponse<AuthenticationResponse> {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(authenticationRequest.username, authenticationRequest.password)
            )

        } catch (e: Exception) {
            throw BadCredentialsException("Bad credentials")
        }
        return try {

            val userDetails = userDetailsService.loadUserByUsername(authenticationRequest.username)
            val jwt = jwtUtil.generateToken(userDetails)
            val expiresIn = jwtUtil.extractExpiresIn(jwt)
            val response = AuthenticationResponse(token = jwt, expiresIn = expiresIn)
            FormatResponse.Success(data = response, message = "Authentication Successfully")
        } catch (e: Exception) {
            FormatResponse.Error(message = e.message.toString())
        }

    }

    @Operation(summary = "Register to app", description = "Register to access app")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful Register"),
            ApiResponse(responseCode = "400", description = "Bad request")
        ]
    )
    @PostMapping(
        value = ["/register"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun registerUser(@Valid @RequestBody registerRequest: RegisterRequest): FormatResponse<String> {
        return try {
            userService.registerUser(registerRequest)
            FormatResponse.Success(message = "User registered successfully", data = "User registered successfully")
        } catch (e: Exception) {
            FormatResponse.Error(message = e.message ?: "Registration failed")
        }
    }
    @Operation(summary = "Logout from app", description = "Logout from access app")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful Logout"),
        ]
    )
    @PostMapping("/logout")
    fun logout(@RequestHeader("Authorization") authorizationHeader: String?, request: HttpServletRequest): FormatResponse<String> {
        return try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                val token = authorizationHeader.substring(7)
                tokenBlacklist.addToken(token)
            }

            request.session.invalidate()
            FormatResponse.Success(message = "Logout successfully", data = "Logout Success")
        } catch (e: Exception) {
            FormatResponse.Error(message = e.message ?: "Logout failed")
        }
    }
}