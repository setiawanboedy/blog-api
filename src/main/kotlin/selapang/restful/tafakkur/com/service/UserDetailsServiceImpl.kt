package selapang.restful.tafakkur.com.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import selapang.restful.tafakkur.com.model.User
import selapang.restful.tafakkur.com.repository.UserRepository

@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository): UserDetailsService {
    override fun loadUserByUsername(usernameOrEmail: String): UserDetails {
        val user: User = userRepository.findUserByUsername(usernameOrEmail)
            ?: userRepository.findByEmail(usernameOrEmail)
            ?: throw UsernameNotFoundException("User not found: $usernameOrEmail")
        return org.springframework.security.core.userdetails.User(user.username, user.password, ArrayList())
    }

}