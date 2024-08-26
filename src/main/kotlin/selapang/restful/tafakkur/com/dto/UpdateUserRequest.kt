package selapang.restful.tafakkur.com.dto

import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile

data class UpdateUserRequest(
    @RequestPart(value = "phoneNumber", required = false)
    val phoneNumber: String?,
    @RequestPart(value = "address", required = false)
    val address: String?,
    val profilePicture: String?
)