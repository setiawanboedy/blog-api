package blog.restful.tafakkur.com.model

import blog.restful.tafakkur.com.dto.response.PostResponse
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "posts")
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var title: String,

    @Column(columnDefinition = "TEXT", nullable = false)
    var content: String,

    @Column(nullable = false)
    var author: String,

    @Column(nullable = false)
    var category: String,

    @ElementCollection
    @CollectionTable(name = "post_tags", joinColumns = [JoinColumn(name = "post_id")])
    @Column(name = "tag")
    var tags: List<String> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: PostStatus = PostStatus.DRAFT,

    var thumbnailImageUrl: String? = null,

    @Column(unique = true, nullable = false)
    var slug: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
){
    fun toPostResponse(): PostResponse {
        return PostResponse(
            id = this.id,
            title = this.title,
            content = this.content,
            author = this.author,
            category = this.category,
            tags = this.tags,
            status = this.status,
            thumbnailImageUrl = this.thumbnailImageUrl,
            slug = this.slug,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
}

enum class PostStatus {
    DRAFT,
    PUBLISHED,
    ARCHIVED
}
