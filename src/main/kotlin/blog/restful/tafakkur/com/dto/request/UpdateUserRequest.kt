package blog.restful.tafakkur.com.dto.request

import org.springframework.web.bind.annotation.RequestPart

data class UpdateUserRequest(
    @RequestPart(value = "phoneNumber", required = false)
    val phoneNumber: String?,
    @RequestPart(value = "address", required = false)
    val address: String?,
    val profilePicture: String?
)