package blog.restful.tafakkur.com.dto.request

import blog.restful.tafakkur.com.model.Post
import blog.restful.tafakkur.com.model.PostStatus
import blog.restful.tafakkur.com.validation.UniqueSlug
import jakarta.validation.constraints.NotBlank

data class UpdatePostRequest(
    val id: Long,

    val title: String? = null,

    val content: String? = null,

    val author: String? = null,

    val category: String? = null,

    val slug: String? = null,

    val thumbnailImageUrl: String? = null,

    val tags: List<String>? = null,

    val status: PostStatus? = null,
)