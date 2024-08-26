package blog.restful.tafakkur.com.service

import blog.restful.tafakkur.com.dto.PostRequest
import blog.restful.tafakkur.com.model.Post
import blog.restful.tafakkur.com.model.PostStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
interface PostService{
    // Buat post
    fun createPost(postRequest: PostRequest): Post
    // Update post
    fun updatePost(id: Long, postRequest: PostRequest): Post?

    //List post
    fun getListPosts(): List<Post>

    //Get postById
    fun getPostById(id: Long): Post?

    //Delete post
    fun deletePost(id: Long)

    // Menemukan semua postingan berdasarkan kategori
    fun findByCategory(category: String): List<Post>

    // Menemukan postingan berdasarkan judul (case-insensitive)
    fun findByTitleIgnoreCase(title: String): List<Post>

    // Menemukan semua postingan yang diterbitkan
    fun findByStatus(status: PostStatus): List<Post>

    // Menemukan postingan berdasarkan penulis
    fun findByAuthor(author: String): List<Post>

    // Menemukan postingan yang dibuat setelah tanggal tertentu
    fun findByCreatedAtAfter(date: LocalDateTime): List<Post>

    // Menemukan postingan berdasarkan kata kunci di konten
    fun findByContentContainingIgnoreCase(keyword: String): List<Post>
}