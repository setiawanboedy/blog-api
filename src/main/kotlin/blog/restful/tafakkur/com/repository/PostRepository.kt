package blog.restful.tafakkur.com.repository

import blog.restful.tafakkur.com.model.Post
import blog.restful.tafakkur.com.model.PostStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    // Menemukan semua postingan berdasarkan kategori
    fun findByCategory(category: String): List<Post>

    // Menemukan postingan berdasarkan judul (case-insensitive)
    fun findByTitleContainingIgnoreCase(title: String): List<Post>

    // Menemukan semua postingan yang diterbitkan
    fun findByStatus(status: PostStatus): List<Post>

    // Menemukan postingan berdasarkan kata kunci di konten
    fun findByContentContainingIgnoreCase(keyword: String): List<Post>

    fun existsBySlug(slug: String): Boolean
}