package blog.restful.tafakkur.com.dto.request

import blog.restful.tafakkur.com.model.Post
import blog.restful.tafakkur.com.model.PostStatus
import blog.restful.tafakkur.com.validation.UniqueSlug
import jakarta.validation.constraints.NotBlank

data class CreatePostRequest(
    @field:NotBlank(message = "required")
    val title: String,

    @field:NotBlank(message = "required")
    val content: String,

    @field:NotBlank(message = "required" )
    val author: String,

    @field:NotBlank(message = "required")
    val category: String,

    @field:NotBlank(message = "required")
    @field:UniqueSlug(message = "Slug must be unique")
    val slug: String,

    val thumbnailImageUrl: String? = null,

    val tags: List<String> = mutableListOf(),

    val status: PostStatus = PostStatus.DRAFT,
){
    fun toPost(): Post{
        return Post(
            title = this.title,
            content = this.content,
            author = this.author,
            category = this.category,
            slug = this.slug,
            thumbnailImageUrl = this.thumbnailImageUrl,
            tags = this.tags,
            status = this.status
        )
    }
}