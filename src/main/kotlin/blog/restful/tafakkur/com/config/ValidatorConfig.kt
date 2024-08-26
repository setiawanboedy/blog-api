package blog.restful.tafakkur.com.config

import blog.restful.tafakkur.com.repository.PostRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import blog.restful.tafakkur.com.repository.UserRepository
import blog.restful.tafakkur.com.validation.UniqueEmailValidator
import blog.restful.tafakkur.com.validation.UniqueSlugValidator
import blog.restful.tafakkur.com.validation.UniqueUsernameValidator

@Configuration
class ValidatorConfig {

    @Bean
    fun uniqueUsernameValidator(userRepository: UserRepository) = UniqueUsernameValidator(userRepository)

    @Bean
    fun uniqueEmailValidator(userRepository: UserRepository) = UniqueEmailValidator(userRepository)

    @Bean
    fun uniqueSlugValidator(postRepository: PostRepository) = UniqueSlugValidator(postRepository)

}