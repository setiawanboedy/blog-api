package blog.restful.tafakkur.com.model

import blog.restful.tafakkur.com.converter.StringListConverter
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

    @Column(nullable = false)
    var subtitle: String,

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    var content: String,

    @Column(nullable = false)
    var author: String,

    @Column(nullable = false)
    var category: String,

    @Convert(converter = StringListConverter::class)
    @Column(nullable = false, columnDefinition = "TEXT")
    var tags: List<String> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: PostStatus = PostStatus.DRAFT,

    var thumbnailImageUrl: String? = null,

    @Column(unique = true, nullable = false)
    var slug: String,

    @Column(nullable = false)
    var main: Boolean = false,

    @Column(nullable = false)
    var sponsored: Boolean = false,

    @Column(nullable = false)
    var popular: Boolean = false,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
){
    fun toPostResponse(): PostResponse {
        return PostResponse(
            id = this.id,
            title = this.title,
            subtitle = this.subtitle,
            content = this.content,
            author = this.author,
            category = this.category,
            tags = this.tags,
            status = this.status,
            thumbnailImageUrl = this.thumbnailImageUrl,
            slug = this.slug,
            main = this.main,
            popular = this.popular,
            sponsored = this.sponsored,
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

enum class Category {
    Programming,
    Technology,
    Design
}
