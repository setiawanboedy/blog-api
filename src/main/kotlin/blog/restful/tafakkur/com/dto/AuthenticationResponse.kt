package blog.restful.tafakkur.com.dto

data class AuthenticationResponse(
    val token: String,
    val expiresIn: Long
)
