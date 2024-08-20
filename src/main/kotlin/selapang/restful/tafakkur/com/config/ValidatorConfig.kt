package selapang.restful.tafakkur.com.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import selapang.restful.tafakkur.com.repository.UserRepository
import selapang.restful.tafakkur.com.validation.UniqueEmailValidator
import selapang.restful.tafakkur.com.validation.UniqueUsernameValidator

@Configuration
class ValidatorConfig {

    @Bean
    fun uniqueUsernameValidator(userRepository: UserRepository) = UniqueUsernameValidator(userRepository)

    @Bean
    fun uniqueEmailValidator(userRepository: UserRepository) = UniqueEmailValidator(userRepository)

}