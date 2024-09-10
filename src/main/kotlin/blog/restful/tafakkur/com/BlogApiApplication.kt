package blog.restful.tafakkur.com

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ComponentScan(basePackages = ["blog.restful.tafakkur.com.repository"])
@EnableJpaRepositories(basePackages = ["blog.restful.tafakkur.com.repository"])
class BlogApiApplication

fun main(args: Array<String>) {
	runApplication<BlogApiApplication>(*args)
}