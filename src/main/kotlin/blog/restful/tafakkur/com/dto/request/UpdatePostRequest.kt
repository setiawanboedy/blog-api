package blog.restful.tafakkur.com.dto.request

import blog.restful.tafakkur.com.model.Post
import blog.restful.tafakkur.com.model.PostStatus
import blog.restful.tafakkur.com.validation.UniqueSlug
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UpdatePostRequest(
    val id: Long,

    val title: String? = null,

    val content: String? = null,

    val author: String? = null,

    val category: String? = null,

    val slug: String? = null,

    val thumbnailImageUrl: String? = null,

    val tags: List<String>? = null,

    @field:NotNull(message = "Status is required and must be one of: [DRAFT, PUBLISHED, ARCHIVED]")
    val status: PostStatus,
)