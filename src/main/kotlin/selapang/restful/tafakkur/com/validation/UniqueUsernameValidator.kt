package selapang.restful.tafakkur.com.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import selapang.restful.tafakkur.com.repository.UserRepository

class UniqueUsernameValidator(
    private val userRepository: UserRepository
): ConstraintValidator<UniqueUsername, String> {
    override fun isValid(username: String?, context: ConstraintValidatorContext?): Boolean {
        if (username.isNullOrBlank()){
            return true
        }
        return userRepository.findUserByUsername(username) == null
    }
}