package blog.restful.tafakkur.com.dto.request

import blog.restful.tafakkur.com.model.Post
import blog.restful.tafakkur.com.model.PostStatus
import blog.restful.tafakkur.com.validation.UniqueSlug
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UpdatePostRequest(
    val title: String,
    val subtitle: String,
    val content: String,
    val category: String,
    val tags: List<String> = mutableListOf(),
    val thumbnailImageUrl: String? = null,
    val main: Boolean,
    val popular: Boolean,
    val sponsored: Boolean,
    val status: String? = null,
)