package blog.restful.tafakkur.com.controller

import blog.restful.tafakkur.com.dto.FormatResponse
import blog.restful.tafakkur.com.dto.request.CreatePostRequest
import blog.restful.tafakkur.com.dto.request.UpdatePostRequest
import blog.restful.tafakkur.com.dto.response.PostResponse
import blog.restful.tafakkur.com.exception.NotFoundException
import blog.restful.tafakkur.com.service.PostService
import jakarta.validation.Valid
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
    fun createPost(@Valid @RequestBody postRequest: CreatePostRequest): FormatResponse<PostResponse> {
        return try {
            val post = postService.createPost(postRequest)
            val response = post.toPostResponse()
            FormatResponse.Success(data = response, message = "Create post successfully")
        }catch (e: Exception){
            FormatResponse.Error(message = "Create post failed: ${e.message}")
        }
    }
    @PutMapping(
        value = ["update/{id}"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun updatePost(
        @PathVariable("id") id: Long,
        @RequestBody postRequest: UpdatePostRequest,
    ): FormatResponse<PostResponse> {
        return try {
            val post = postService.updatePost(id, postRequest)
            val response = post?.toPostResponse()
            FormatResponse.Success(data = response, message = "Update post successfully")
        }catch (exception: NotFoundException){
            throw exception
//            FormatResponse.Error(message = "Update post failed")
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