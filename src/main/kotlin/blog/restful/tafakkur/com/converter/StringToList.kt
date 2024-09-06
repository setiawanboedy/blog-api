package blog.restful.tafakkur.com.converter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

fun convertStringToList(dbData: String?): List<String> {
    val objectMapper = jacksonObjectMapper()
    return dbData?.let { objectMapper.readValue(it, Array<String>::class.java).toList() } ?: emptyList()
}