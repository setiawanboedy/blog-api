package selapang.restful.tafakkur.com.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import selapang.restful.tafakkur.com.dto.FormatResponse
import selapang.restful.tafakkur.com.dto.UserResponse
import selapang.restful.tafakkur.com.service.UserService

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
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
    fun getCurrentUser(): FormatResponse<UserResponse>{
        return try {
            val user = userService.getCurrentUser()
            if (user != null){
                val response = UserResponse(id = user.id, username = user.username, email = user.email)
                FormatResponse.Success(data = response, message = "Get user successfully")
            }else {
                FormatResponse.Error(code = 404, message = "User not found")
            }
        }catch (e: Exception){
            FormatResponse.Error(message = "${e.message}")
        }
    }

}