package blog.restful.tafakkur.com.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import blog.restful.tafakkur.com.dto.request.RegisterRequest
import blog.restful.tafakkur.com.dto.request.UpdateUserRequest
import blog.restful.tafakkur.com.model.User
import blog.restful.tafakkur.com.repository.UserRepository
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun registerUser(registerRequest: RegisterRequest): User{

        val user = User(
            username = registerRequest.username,
            email = registerRequest.email,
            password = passwordEncoder.encode(registerRequest.password),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        return userRepository.save(user)
    }

    fun getCurrentUser(): User?{
        val authentication = SecurityContextHolder.getContext().authentication
        val username = when (val principal = authentication.principal){
            is UserDetails -> principal.username
            is String -> principal
            else -> null
        }


        return if (username != null){
            userRepository.findUserByUsername(username)
        }else{
            null
        }
    }

    fun updateCurrentUser(updateUserRequest: UpdateUserRequest, profilePictureUrl: String?): User?{
        val currentUser = getCurrentUser()

        currentUser?.phoneNumber = updateUserRequest.phoneNumber
        currentUser?.address = updateUserRequest.address
        currentUser?.profilePicture = profilePictureUrl

        return currentUser?.let { userRepository.save(it) }
    }
}