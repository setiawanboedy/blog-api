package blog.restful.tafakkur.com.dto

data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val phoneNumber: String?,
    val address: String?,
    val profilePicture: String?,
)
