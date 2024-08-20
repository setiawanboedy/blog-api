package selapang.restful.tafakkur.com.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import selapang.restful.tafakkur.com.repository.UserRepository

class UniqueEmailValidator(
    private val userRepository: UserRepository
): ConstraintValidator<UniqueEmail, String> {
    override fun isValid(email: String?, context: ConstraintValidatorContext?): Boolean {
        if (email.isNullOrBlank()){
            return true
        }
        return userRepository.findByEmail(email) == null
    }
}