package blog.restful.tafakkur.com.dto.response

data class AuthenticationResponse(
    val token: String,
    val expiresIn: Long
)
