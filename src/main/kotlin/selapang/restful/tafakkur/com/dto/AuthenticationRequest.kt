package selapang.restful.tafakkur.com.dto

import jakarta.validation.constraints.NotBlank
import selapang.restful.tafakkur.com.validation.UniqueUsername

data class AuthenticationRequest (
    @field:NotBlank(message = "Username or email is required")
    @field:UniqueUsername
    val username: String,

    @field:NotBlank(message = "Password is required")
    val password: String
)