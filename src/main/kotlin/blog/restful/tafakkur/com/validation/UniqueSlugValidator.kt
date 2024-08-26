package blog.restful.tafakkur.com.validation

import blog.restful.tafakkur.com.repository.PostRepository
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class UniqueSlugValidator(private val postRepository: PostRepository) : ConstraintValidator<UniqueSlug, String> {

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) {
            return true
        }
        return !postRepository.existsBySlug(value)
    }
}