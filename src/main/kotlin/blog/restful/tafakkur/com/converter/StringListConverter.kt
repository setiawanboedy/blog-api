package blog.restful.tafakkur.com.converter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class StringListConverter : AttributeConverter<List<String>, String> {
    private val objectMapper = jacksonObjectMapper()

    override fun convertToDatabaseColumn(attribute: List<String>?): String {
        return attribute?.let { objectMapper.writeValueAsString(it) } ?: "[]"
    }

    override fun convertToEntityAttribute(dbData: String?): List<String> {
        return dbData?.let { objectMapper.readValue(it, Array<String>::class.java).toList() } ?: emptyList()
    }

}