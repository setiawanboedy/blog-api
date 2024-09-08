package blog.restful.tafakkur.com.config

import blog.restful.tafakkur.com.converter.StringListConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConverterConfig {

    @Bean
    fun stringListConverter(): StringListConverter {
        return StringListConverter()
    }
}