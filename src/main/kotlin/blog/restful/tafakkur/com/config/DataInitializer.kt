package blog.restful.tafakkur.com.config

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import blog.restful.tafakkur.com.model.User
import blog.restful.tafakkur.com.repository.UserRepository

@Component
class DataInitializer(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
): ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        val adminExists = userRepository.findUserByUsername("admin") != null

        if (!adminExists){
            val defaultUser = User(
                username = "admin",
                email = "admin@admin.com",
                password = passwordEncoder.encode("password"),
            )
            userRepository.save(defaultUser)
        }
    }

}