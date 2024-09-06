package blog.restful.tafakkur.com.dto.request

import blog.restful.tafakkur.com.model.Post
import blog.restful.tafakkur.com.model.PostStatus

data class CreatePostRequest(
    val title: String,
    val subtitle: String,
    val content: String,
    val category: String,
    val tags: List<String> = mutableListOf(),
    val status: String = PostStatus.DRAFT.name,
    val thumbnailImageUrl: String? = null,
){
    fun toPost(): Post{
        return Post(
            title = this.title,
            subtitle = this.subtitle,
            content = this.content,
            author = "",
            category = this.category,
            slug = "",
            thumbnailImageUrl = this.thumbnailImageUrl,
            tags = this.tags,
            status = PostStatus.valueOf(this.status)
        )
    }
}