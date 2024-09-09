package blog.restful.tafakkur.com

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["blog.restful.tafakkur.com.repository"])
class BlogApiApplication

fun main(args: Array<String>) {
	runApplication<BlogApiApplication>(*args) {
		setBannerMode(Banner.Mode.OFF)
	}
}