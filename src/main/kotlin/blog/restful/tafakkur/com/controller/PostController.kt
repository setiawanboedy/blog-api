package blog.restful.tafakkur.com.controller

import blog.restful.tafakkur.com.dto.FormatResponse
import blog.restful.tafakkur.com.dto.request.CreatePostRequest
import blog.restful.tafakkur.com.dto.request.UpdatePostRequest
import blog.restful.tafakkur.com.dto.response.PostResponse
import blog.restful.tafakkur.com.exception.NotFoundException
import blog.restful.tafakkur.com.exception.UnauthorizedException
import blog.restful.tafakkur.com.service.PostService
import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/posts")
class PostController(
    private val postService: PostService
) {
    @PostMapping(
        value = ["create"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun createPost(
        @Valid
        @RequestBody
        postRequest: CreatePostRequest
    ): ResponseEntity<FormatResponse<PostResponse>> {
        return try {
            val post = postService.createPost(postRequest)
            val response = post.toPostResponse()
            ResponseEntity.ok(FormatResponse.Success(data = response, message = "Create post successfully"))
        } catch (e: MethodArgumentNotValidException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FormatResponse.Error(message = e.message))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(FormatResponse.Error(message = "Create post failed"))
        }
    }

    @PutMapping(
        value = ["/{id}/update"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun updatePost(
        @PathVariable("id") id: Long,
        @RequestBody postRequest: UpdatePostRequest,
    ): ResponseEntity<FormatResponse<PostResponse>> {
        return try {
            val post = postService.updatePost(id, postRequest)
            val response = post?.toPostResponse()
            ResponseEntity.ok(FormatResponse.Success(data = response, message = "Update post successfully"))
        } catch (exception: NotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(FormatResponse.Error(message = "${exception.message}"))
        } catch (exception: HttpMessageNotReadableException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FormatResponse.Error(message = "${exception.message}"))

        } catch (exception: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(FormatResponse.Error(message = "Update post failed"))

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
        value = ["list"],
        produces = ["application/json"],
    )
    fun getListPosts(
        @RequestParam filter: MutableMap<String, String>,
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int
    ): ResponseEntity<FormatResponse<List<PostResponse>>> {
        return try {
            val isPage = filter.containsKey("page")
            val isSize = filter.containsKey("size")
            val posts = if (isSize && isPage){
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