package blog.restful.tafakkur.com.service.impl


import blog.restful.tafakkur.com.dto.request.CreatePostRequest
import blog.restful.tafakkur.com.dto.request.UpdatePostRequest
import blog.restful.tafakkur.com.exception.NotFoundException
import blog.restful.tafakkur.com.model.Post
import blog.restful.tafakkur.com.model.PostStatus
import blog.restful.tafakkur.com.repository.PostRepository
import blog.restful.tafakkur.com.service.PostService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class PostServiceImpl(
    private val postRepository: PostRepository
) : PostService {
    // Buat post
    override fun createPost(postRequest: CreatePostRequest): Post {
        val post = postRequest.toPost()
        return postRepository.save(post)
    }

    // Update post
    override fun updatePost(id: Long, postRequest: UpdatePostRequest): Post? {
        val post = getUpdatePost(id, postRequest)
        return post.let { postRepository.save(it) }
    }

    //List post
    override fun getListPosts(pageable: Pageable): Page<Post> {
        return postRepository.findAll(pageable)
    }

    //List post filter
    override fun getListPostsByFilter(filter: MutableMap<String, String>?): List<Post> {
        return findListByFilter(filter)
    }

    //Get postById
    override fun getPostById(id: Long): Post? {
        return findPostByIdOrThrowNotFound(id)
    }

    //Delete post
    override fun deletePost(id: Long) {
        findPostByIdOrThrowNotFound(id)
        return postRepository.deleteById(id)
    }

    // Menemukan semua postingan berdasarkan kategori
    override fun findByCategory(category: String): List<Post> {
        return postRepository.findByCategory(category)
    }

    // Menemukan postingan berdasarkan judul (case-insensitive)
    override fun findByTitleIgnoreCase(title: String): List<Post> {
        return postRepository.findByTitleIgnoreCase(title)
    }

    // Menemukan semua postingan yang diterbitkan
    override fun findByStatus(status: PostStatus): List<Post> {
        return postRepository.findByStatus(status)
    }

    // Menemukan postingan berdasarkan penulis
    override fun findByAuthor(author: String): List<Post> {
        return postRepository.findByAuthor(author)
    }

    // Menemukan postingan yang dibuat setelah tanggal tertentu
    override fun findByCreatedAtAfter(date: LocalDateTime): List<Post> {
        return postRepository.findByCreatedAtAfter(date)
    }

    // Menemukan postingan berdasarkan kata kunci di konten
    override fun findByContentContainingIgnoreCase(keyword: String): List<Post> {
        return postRepository.findByContentContainingIgnoreCase(keyword)
    }

    private fun findPostByIdOrThrowNotFound(id: Long): Post {
        val post = postRepository.findById(id).orElseThrow { NotFoundException("Post with ID $id not found") }
        return post;
    }

    private fun findListByFilter(params: MutableMap<String, String>? = null): List<Post> {
        return when {
            params?.containsKey("category") == true -> {
                val category = params["category"] ?: ""
                postRepository.findByCategory(category)
            }

            params?.containsKey("title") == true -> {
                val title = params["title"] ?: ""
                postRepository.findByTitleIgnoreCase(title)
            }

            params?.containsKey("status") == true -> {
                val status = params["status"]?.let {
                    PostStatus.valueOf(it.uppercase(Locale.getDefault()))
                }
                if (status != null) {
                    postRepository.findByStatus(status)

                } else {
                    postRepository.findAll()
                }
            }

            else -> postRepository.findAll()
        }
    }

    private fun getUpdatePost(id: Long, request: UpdatePostRequest): Post {
        val post = findPostByIdOrThrowNotFound(id)
        post.let {
            it.title = request.title ?: it.title
            it.content = request.content ?: it.content
            it.author = request.author ?: it.author
            it.category = request.category ?: it.category
            it.slug = request.slug ?: it.slug
            it.thumbnailImageUrl = request.thumbnailImageUrl ?: it.thumbnailImageUrl
            it.tags = request.tags ?: it.tags
            it.status = request.status ?: it.status
        }

        return post
    }
}
