package selapang.restful.tafakkur.com.controller

import jakarta.validation.Valid
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import selapang.restful.tafakkur.com.config.JwtUtil
import selapang.restful.tafakkur.com.dto.RegisterRequest
import selapang.restful.tafakkur.com.dto.AuthenticationRequest
import selapang.restful.tafakkur.com.dto.AuthenticationResponse
import selapang.restful.tafakkur.com.dto.FormatResponse
import selapang.restful.tafakkur.com.exception.BadCredentialsException
import selapang.restful.tafakkur.com.service.UserService

@RestController
@RequestMapping("/api/auth")
@Validated
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService,
    private val userService: UserService
) {

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

    @PostMapping(
        value = ["/register"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun registerUser(@Valid @RequestBody registerRequest: RegisterRequest): FormatResponse<String> {
        return try {
            userService.registerUser(registerRequest)
            FormatResponse.Success(message = "User registered successfully", data = "User registered successfully")
        } catch (e: IllegalArgumentException) {
            FormatResponse.Error(message = e.message ?: "Registration failed")
        }
    }
}