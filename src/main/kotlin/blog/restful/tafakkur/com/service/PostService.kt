package blog.restful.tafakkur.com.service

import blog.restful.tafakkur.com.dto.request.CreatePostRequest
import blog.restful.tafakkur.com.dto.request.UpdatePostRequest
import blog.restful.tafakkur.com.model.Post
import blog.restful.tafakkur.com.model.PostStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
interface PostService{
    // Buat post
    fun createPost(postRequest: CreatePostRequest): Post
    // Update post
    fun updatePost(id: Long, postRequest: UpdatePostRequest): Post?

    //List post
    fun getListPosts(pageable: Pageable): Page<Post>

    //List post filter
    fun getListPostsByFilter(filter: MutableMap<String, String>?): List<Post>

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

    // Menemukan postingan berdasarkan kata kunci di konten
    fun findByContentContainingIgnoreCase(keyword: String): List<Post>

}