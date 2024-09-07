package blog.restful.tafakkur.com.controller

import blog.restful.tafakkur.com.converter.StringListConverter
import blog.restful.tafakkur.com.dto.FormatResponse
import blog.restful.tafakkur.com.dto.request.CreatePostRequest
import blog.restful.tafakkur.com.dto.request.UpdatePostRequest
import blog.restful.tafakkur.com.dto.response.DashboardResponse
import blog.restful.tafakkur.com.dto.response.PostResponse
import blog.restful.tafakkur.com.exception.NotFoundException
import blog.restful.tafakkur.com.exception.UnauthorizedException
import blog.restful.tafakkur.com.model.PostStatus
import blog.restful.tafakkur.com.service.PostService
import blog.restful.tafakkur.com.service.StorageService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("api/posts")
class PostController(
    private val postService: PostService,
    private val storageService: StorageService,
    private val stringListConverter: StringListConverter
) {
    @PostMapping(
        value = ["/create"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createPost(
        @RequestPart(value = "title", required = true)
        title: String,
        @RequestPart(value = "subtitle", required = true)
        subtitle: String,
        @RequestPart(value = "content", required = true)
        content: String,
        @RequestPart(value = "category", required = true)
        category: String,
        @RequestPart(value = "tags", required = false)
        tags: String? = null,
        @RequestPart(value = "main", required = false)
        main: String,
        @RequestPart(value = "popular", required = false)
        popular: String,
        @RequestPart(value = "sponsored", required = false)
        sponsored: String,
        @RequestPart(value = "status", required = false)
        status: String = PostStatus.DRAFT.name,
        @RequestPart(value = "thumbnailLinkUrl", required = false)
        thumbnailLinkUrl: String? = null,
        @RequestPart(value = "thumbnailImageUrl", required = false) file: MultipartFile?,
    ): ResponseEntity<FormatResponse<PostResponse>> {
        var thumbnailUrl: String? = thumbnailLinkUrl
        file?.let {
            thumbnailUrl = storageService.storeFile(it, subfolder = "posts")
        }
        return try {
            val finalTags = stringListConverter.convertToEntityAttribute(tags)
            val applyPostRequest = CreatePostRequest(
                title = title,
                subtitle = subtitle,
                content = content,
                category = category,
                tags = finalTags,
                main = main.toBoolean(),
                popular = popular.toBoolean(),
                sponsored = sponsored.toBoolean(),
                status = status,
                thumbnailImageUrl = thumbnailUrl
            )
            val post = postService.createPost(applyPostRequest)
            val response = post.toPostResponse()
            ResponseEntity.ok(FormatResponse.Success(data = response, message = "Create post successfully"))
        } catch (e: MethodArgumentNotValidException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FormatResponse.Error(message = e.message))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(FormatResponse.Error(message = "Create post failed: ${e.message}"))
        }
    }

    @PostMapping(
        value = ["/{id}/update"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updatePost(
        @PathVariable("id") id: Long,
        @RequestPart(value = "title", required = true)
        title: String,
        @RequestPart(value = "subtitle", required = true)
        subtitle: String,
        @RequestPart(value = "content", required = true)
        content: String,
        @RequestPart(value = "category", required = true)
        category: String,
        @RequestPart(value = "tags", required = false)
        tags: String? = null,
        @RequestPart(value = "main", required = false)
        main: String,
        @RequestPart(value = "popular", required = false)
        popular: String,
        @RequestPart(value = "sponsored", required = false)
        sponsored: String,
        @RequestPart(value = "status", required = false)
        status: String = PostStatus.DRAFT.name,
        @RequestPart(value = "thumbnailLinkUrl", required = false)
        thumbnailLinkUrl: String? = null,
        @RequestPart(value = "thumbnailImageUrl", required = false) file: MultipartFile?,
    ): ResponseEntity<FormatResponse<PostResponse>> {
        return try {
            var thumbnailUrl: String? = thumbnailLinkUrl
            file?.let {
                thumbnailUrl = storageService.storeFile(it, subfolder = "posts")
            }

            val finalTags = stringListConverter.convertToEntityAttribute(tags)
            val applyPostRequest = UpdatePostRequest(
                title = title,
                subtitle = subtitle,
                content = content,
                category =  category,
                tags =  finalTags,
                main = main.toBoolean(),
                popular = popular.toBoolean(),
                sponsored = sponsored.toBoolean(),
                status =  status,
                thumbnailImageUrl =  thumbnailUrl
            )
            val post = postService.updatePost(id, applyPostRequest)
            val response = post?.toPostResponse()
            ResponseEntity.ok(FormatResponse.Success(data = response, message = "Update post successfully"))
        } catch (exception: NotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(FormatResponse.Error(message = "${exception.message}"))
        } catch (exception: HttpMessageNotReadableException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FormatResponse.Error(message = "${exception.message}"))

        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(FormatResponse.Error(message = "Update post failed: ${e.message}"))

        }
    }

    @GetMapping(
        value = ["/{id}/detail"],
        produces = ["application/json"],
    )
    fun detailPost(
        @PathVariable("id") id: Long,
    ): ResponseEntity<FormatResponse<PostResponse>> {
        return try {
            val post = postService.getPostById(id)
            val response = post?.toPostResponse()
            ResponseEntity.ok(FormatResponse.Success(data = response, message = "Get post successfully"))
        } catch (exception: NotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(FormatResponse.Error(message = "${exception.message}"))
        } catch (exception: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(FormatResponse.Error(message = "Get post failed"))

        }
    }

    @GetMapping(
        value = ["/dashboard"],
        produces = ["application/json"],
    )
    fun dashboardPost(): ResponseEntity<FormatResponse<DashboardResponse>> {
        return try {
            val response = postService.getPostDashboard()
            ResponseEntity.ok(FormatResponse.Success(data = response, message = "Get dashboard successfully"))
        } catch (exception: NotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(FormatResponse.Error(message = "${exception.message}"))
        } catch (exception: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(FormatResponse.Error(message = "Get dashboard failed"))

        }
    }

    @DeleteMapping(
        value = ["/{id}/delete"],
    )
    fun deletePost(
        @PathVariable("id") id: Long,
    ): ResponseEntity<FormatResponse<String>> {
        return try {
            postService.deletePost(id)
            ResponseEntity.ok(FormatResponse.Success(data = "Success", message = "Delete post successfully"))
        } catch (exception: NotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(FormatResponse.Error(message = "${exception.message}"))
        } catch (exception: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(FormatResponse.Error(message = "Delete post failed"))

        }
    }

    @GetMapping(
        value = ["/list"],
        produces = ["application/json"],
    )
    fun getListPosts(
        @RequestParam filter: MutableMap<String, String>,
    ): ResponseEntity<FormatResponse<List<PostResponse>>> {
        return try {
            val isPage = filter.containsKey("page")
            val isSize = filter.containsKey("size")
            val posts = if (isSize && isPage){
                val page = filter["page"]?.toInt() ?: 0
                val size = filter["size"]?.toInt() ?: 0
                val pageable: Pageable = PageRequest.of(page, size)
                postService.getListPosts(pageable)

            }else{
                postService.getListPostsByFilter(filter)
            }
            val listPosts = mutableListOf<PostResponse>()
            posts.forEach { data ->
                listPosts.add(data.toPostResponse())
            }
            ResponseEntity.ok(FormatResponse.Success(data = listPosts, message = "Get list post successfully"))
        } catch (exception: UnauthorizedException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(FormatResponse.Error(message = "Unauthorized"))
        } catch (exception: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FormatResponse.Error(message = "Get list post failed"))
        }
    }

}