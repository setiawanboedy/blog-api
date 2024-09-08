package blog.restful.tafakkur.com.controller

import blog.restful.tafakkur.com.dto.FormatResponse
import blog.restful.tafakkur.com.dto.response.PostResponse
import blog.restful.tafakkur.com.exception.NotFoundException
import blog.restful.tafakkur.com.exception.UnauthorizedException
import blog.restful.tafakkur.com.service.PostService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/front/posts")
class FrontPostController(
    private val postService: PostService,
) {

    @GetMapping(
        value = ["/{id}/detail"],
        produces = ["application/json"],
    )
    fun detailFrontPost(
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
        value = ["/list"],
        produces = ["application/json"],
    )
    fun getListFrontPosts(
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