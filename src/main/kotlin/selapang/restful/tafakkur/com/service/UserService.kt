package selapang.restful.tafakkur.com.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import selapang.restful.tafakkur.com.dto.RegisterRequest
import selapang.restful.tafakkur.com.model.User
import selapang.restful.tafakkur.com.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun registerUser(registerRequest: RegisterRequest): User{

        val user = User(
            username = registerRequest.username,
            email = registerRequest.email,
            password = passwordEncoder.encode(registerRequest.password)
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
}