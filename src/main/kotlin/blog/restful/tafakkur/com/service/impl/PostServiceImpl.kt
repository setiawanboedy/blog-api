package blog.restful.tafakkur.com.service.impl


import blog.restful.tafakkur.com.dto.PostRequest
import blog.restful.tafakkur.com.exception.NotFoundException
import blog.restful.tafakkur.com.model.Post
import blog.restful.tafakkur.com.model.PostStatus
import blog.restful.tafakkur.com.repository.PostRepository
import blog.restful.tafakkur.com.service.PostService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PostServiceImpl(
    private val postRepository: PostRepository
): PostService {
    // Buat post
    override fun createPost(postRequest: PostRequest): Post{
        val post = postRequest.toPost()
        return postRepository.save(post)
    }
    // Update post
    override fun updatePost(id: Long, postRequest: PostRequest): Post?{
        val post = getUpdatePost(id, postRequest)
        return post?.let { postRepository.save(it) }
    }

    //List post
    override fun getListPosts(): List<Post>{
        return postRepository.findAll()
    }

    //Get postById
    override fun getPostById(id: Long): Post?{
        return findProductByIdOrThrowNotFound(id)
    }

    //Delete post
    override fun deletePost(id: Long) {
        return postRepository.deleteById(id)
    }

    // Menemukan semua postingan berdasarkan kategori
    override fun findByCategory(category: String): List<Post>{
        return postRepository.findByCategory(category)
    }

    // Menemukan postingan berdasarkan judul (case-insensitive)
    override fun findByTitleIgnoreCase(title: String): List<Post>{
        return postRepository.findByTitleIgnoreCase(title)
    }

    // Menemukan semua postingan yang diterbitkan
    override fun findByStatus(status: PostStatus): List<Post>{
        return postRepository.findByStatus(status)
    }

    // Menemukan postingan berdasarkan penulis
    override fun findByAuthor(author: String): List<Post>{
        return postRepository.findByAuthor(author)
    }

    // Menemukan postingan yang dibuat setelah tanggal tertentu
    override fun findByCreatedAtAfter(date: LocalDateTime): List<Post>{
        return postRepository.findByCreatedAtAfter(date)
    }

    // Menemukan postingan berdasarkan kata kunci di konten
    override fun findByContentContainingIgnoreCase(keyword: String): List<Post>{
        return postRepository.findByContentContainingIgnoreCase(keyword)
    }

    private fun findProductByIdOrThrowNotFound(id: Long): Post {
        val post = postRepository.findByIdOrNull(id)
        if (post == null) {
            throw NotFoundException()
        } else {
            return post;
        }
    }

    private fun getUpdatePost(id: Long, request: PostRequest): Post?{
        val post = getPostById(id)
        post?.id = id
        post?.title = request.title
        post?.content = request.content
        post?.author = request.author
        post?.category = request.category
        post?.slug = request.slug
        post?.thumbnailImageUrl = request.thumbnailImageUrl
        post?.tags = request.tags
        post?.status = request.status

        return post
    }
}