package blog.restful.tafakkur.com.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import blog.restful.tafakkur.com.dto.FormatResponse
import blog.restful.tafakkur.com.dto.UpdateUserRequest
import blog.restful.tafakkur.com.dto.UserResponse
import blog.restful.tafakkur.com.exception.UnauthorizedException
import blog.restful.tafakkur.com.service.StorageService
import blog.restful.tafakkur.com.service.UserService

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val storageService: StorageService
) {

    @Operation(summary = "Get user app", description = "Get user by jwt token")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful user"),
            ApiResponse(responseCode = "404", description = "User not found")
        ]
    )
    @GetMapping(
        value = ["/me"],
        produces = ["application/json"],
    )
    fun getCurrentUser(): FormatResponse<UserResponse> {
        return try {
            val user = userService.getCurrentUser()
            if (user != null) {
                val response = user.toUserResponse()
                FormatResponse.Success(data = response, message = "Get user successfully")
            } else {
                throw UnauthorizedException("Unauthorized")
            }
        } catch (e: Exception) {
            throw UnauthorizedException("Unauthorized")
        }
    }

    @Operation(summary = "Update user app", description = "Update user by jwt token")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Update user successful"),
            ApiResponse(responseCode = "404", description = "User not found")
        ]
    )
    @PutMapping(
        value = ["/me"],
    )
    fun updateCurrentUser(
        request: UpdateUserRequest,
        @RequestPart(value = "picture", required = false) file: MultipartFile?,
    ): FormatResponse<UserResponse> {

        var profilePictureUrl: String? = null
        file?.let {
            profilePictureUrl = storageService.storeFile(it, subfolder = "users", replace = true)
        }
        return try {
            val updateUserRequest = UpdateUserRequest(
                phoneNumber = request.phoneNumber,
                address = request.address,
                profilePicture = profilePictureUrl
            )
            val updateUser = userService.updateCurrentUser(updateUserRequest, profilePictureUrl)
            if (updateUser != null) {
                val response = updateUser.toUserResponse()
                FormatResponse.Success(data = response, message = "Update user successfully")
            } else {
                throw UnauthorizedException("Unauthorized")
            }
        } catch (e: Exception) {
            FormatResponse.Error(message = "${e.message}")
        }
    }

}