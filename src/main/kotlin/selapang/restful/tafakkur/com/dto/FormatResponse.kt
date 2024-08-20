package selapang.restful.tafakkur.com.dto

sealed class FormatResponse<T>{
    data class Success<T>(val status: String = "Success", val code: Int = 200, val message: String, val data: T? = null): FormatResponse<T>()
    data class Error<T>(val status: String = "Error", val code: Int = 400, val message: String, val data: T? = null): FormatResponse<T>()
}
