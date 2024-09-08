package blog.restful.tafakkur.com.service.impl


import blog.restful.tafakkur.com.dto.request.CreatePostRequest
import blog.restful.tafakkur.com.dto.request.UpdatePostRequest
import blog.restful.tafakkur.com.dto.response.DashboardResponse
import blog.restful.tafakkur.com.exception.NotFoundException
import blog.restful.tafakkur.com.model.Category
import blog.restful.tafakkur.com.model.Post
import blog.restful.tafakkur.com.model.PostStatus
import blog.restful.tafakkur.com.repository.PostRepository
import blog.restful.tafakkur.com.service.PostService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class PostServiceImpl(
    private val postRepository: PostRepository,
) : PostService {
    // Buat post
    override fun createPost(postRequest: CreatePostRequest): Post {
        val post = postRequest.toPost().apply {
            author = "Budi Setiawan"
            slug = generateUniqueSlug(title)
        }
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

    override fun getPostDashboard(): DashboardResponse {
        val technology = findListByFilter(mutableMapOf("category" to Category.Technology.name))
        val programming = findListByFilter(mutableMapOf("category" to Category.Programming.name))
        val design = findListByFilter(mutableMapOf("category" to Category.Design.name))
        return DashboardResponse(
            technology = technology.size,
            programming = programming.size,
            design = design.size
        )
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
        return postRepository.findByTitleContainingIgnoreCase(title)
    }

    // Menemukan semua postingan yang diterbitkan
    override fun findByStatus(status: PostStatus): List<Post> {
        return postRepository.findByStatus(status)
    }

    // Menemukan postingan berdasarkan kata kunci di konten
    override fun findByContentContainingIgnoreCase(keyword: String): List<Post> {
        return postRepository.findByContentContainingIgnoreCase(keyword)
    }

    override fun findByMain(main: Boolean): List<Post> {
        return postRepository.findByMain(main)
    }

    override fun findBySponsored(sponsored: Boolean): List<Post> {
        return postRepository.findBySponsored(sponsored)
    }

    override fun findByPopular(popular: Boolean): List<Post> {
        return postRepository.findByPopular(popular)
    }

    private fun generateUniqueSlug(title: String): String {
        val slug = generateSlug(title)
        var uniqueSlug = slug
        var counter = 1

        while (postRepository.existsBySlug(uniqueSlug)) {
            uniqueSlug = "$slug-$counter"
            counter++
        }

        return uniqueSlug
    }


    private fun generateSlug(title: String): String {
        return title
            .trim()
            .lowercase()
            .replace(Regex("[^a-z0-9\\s-]"), "")
            .replace("\\s+".toRegex(), "-")
            .replace("-+".toRegex(), "-")
    }

    private fun findPostByIdOrThrowNotFound(id: Long): Post {
        val post = postRepository.findById(id).orElseThrow { NotFoundException("Post with ID $id not found") }
        return post;
    }

    private fun findListByFilter(params: MutableMap<String, String>? = null): List<Post> {
        return when {
            params?.containsKey("category") == true -> {
                val category = params["category"] ?: ""
                findByCategory(category)
            }

            params?.containsKey("title") == true -> {
                val title = params["title"] ?: ""
                findByTitleIgnoreCase(title)
            }

            params?.containsKey("keyword") == true -> {
                val keyword = params["keyword"] ?: ""
                findByContentContainingIgnoreCase(keyword)
            }

            params?.containsKey("main") == true -> {
                val main = params["main"]
                findByMain(main.toBoolean())
            }

            params?.containsKey("popular") == true -> {
                val popular = params["popular"]
                findByPopular(popular.toBoolean())
            }

            params?.containsKey("sponsored") == true -> {
                val sponsored = params["sponsored"]
                findBySponsored(sponsored.toBoolean())
            }

            params?.containsKey("status") == true -> {
                val status = params["status"]?.let {
                    PostStatus.valueOf(it.uppercase(Locale.getDefault()))
                }
                if (status != null) {
                    findByStatus(status)

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
            it.title = request.title
            it.content = request.content
            it.author = it.author
            it.category = request.category
            it.slug = it.slug
            it.thumbnailImageUrl = request.thumbnailImageUrl ?: it.thumbnailImageUrl
            it.tags = request.tags
            it.main = request.main
            it.popular = request.popular
            it.sponsored = request.sponsored
            it.status = PostStatus.valueOf(request.status ?: it.status.name)
        }

        return post
    }
}
