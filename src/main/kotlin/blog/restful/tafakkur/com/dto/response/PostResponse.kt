package blog.restful.tafakkur.com.dto

import blog.restful.tafakkur.com.model.PostStatus
import java.time.LocalDateTime

data class PostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val author: String,
    val category: String,
    val tags: List<String>,
    val status: PostStatus,
    val thumbnailImageUrl: String?,
    val slug: String,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
)
