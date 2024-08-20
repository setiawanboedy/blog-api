package selapang.restful.tafakkur.com.dto

data class AuthenticationResponse(
    val token: String,
    val expiresIn: Long
)
