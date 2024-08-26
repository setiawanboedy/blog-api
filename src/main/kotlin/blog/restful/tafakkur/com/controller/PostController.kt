package blog.restful.tafakkur.com.controller

import blog.restful.tafakkur.com.dto.FormatResponse
import blog.restful.tafakkur.com.dto.PostRequest
import blog.restful.tafakkur.com.dto.PostResponse
import blog.restful.tafakkur.com.model.Post
import blog.restful.tafakkur.com.service.PostService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
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
    fun createPost(@Valid @RequestBody postRequest: PostRequest): FormatResponse<PostResponse> {
        return try {
            val post = postService.createPost(postRequest)
            val response = post.toPostResponse()
            FormatResponse.Success(data = response, message = "Create post successfully")
        }catch (e: Exception){
            FormatResponse.Error(message = "Create post failed: ${e.message}")
        }
    }
    @PostMapping(
        value = ["update"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun updatePost(
        @Valid
        @RequestParam("id") id: Long,
        @RequestBody postRequest: PostRequest,
    ): FormatResponse<PostResponse> {
        return try {
            val post = postService.updatePost(id, postRequest)
            val response = post?.toPostResponse()
            FormatResponse.Success(data = response, message = "Update post successfully")
        }catch (e: Exception){
            FormatResponse.Error(message = "Update post failed")
        }
    }

    @GetMapping(
        value = ["list"],
        produces = ["application/json"],
    )
    fun getListPosts(): FormatResponse<List<PostResponse>> {
        return try {
            val posts = postService.getListPosts()
            val listPosts = mutableListOf<PostResponse>()
            posts.forEach { data ->
                listPosts.add(data.toPostResponse())
            }
            FormatResponse.Success(data = listPosts, message = "Get list post successfully")
        }catch (e: Exception){
            FormatResponse.Error(message = "Get list post failed")
        }
    }
}