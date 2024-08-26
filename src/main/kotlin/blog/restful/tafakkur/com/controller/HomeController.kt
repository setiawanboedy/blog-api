package blog.restful.tafakkur.com.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController(
) {

    @GetMapping(
        value = ["/"]
    )
    fun home(): Version {
        return Version(name = "Blog", version = "1.0")
    }

}

data class Version(
    val name: String,
    val version: String
)