package blog.restful.tafakkur.com.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.multipart.MaxUploadSizeExceededException
import blog.restful.tafakkur.com.dto.FormatResponse
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<FormatResponse<MutableMap<String, String>>> {
        val errors: MutableMap<String, String> = HashMap()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage ?: "Invalid"
            errors[fieldName] = errorMessage
        }
        val body = FormatResponse.Error(
            code = 400,
            message = "Bad Request",
            data = errors
        )
        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidEnumException(
        ex: HttpMessageNotReadableException,
        request: WebRequest
    ): ResponseEntity<FormatResponse<Nothing>> {
        val errorMessage = "Status accepted values are: [DRAFT, PUBLISHED, ARCHIVED]"
        return ResponseEntity(
            FormatResponse.Error(status = "Error", code = 400, message = errorMessage),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(ex: BadCredentialsException): ResponseEntity<FormatResponse.Error<Nothing>> {
        val body = FormatResponse.Error(
            code = 400,
            message = ex.message ?: "Bad credentials",
            data = null
        )

        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(ex: UnauthorizedException): ResponseEntity<FormatResponse.Error<Nothing>> {
        val body = FormatResponse.Error(
            code = 401,
            message = ex.message ?: "Unauthorized",
            data = null
        )
        return ResponseEntity(body, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<FormatResponse.Error<Nothing>> {
        val body = FormatResponse.Error(
            code = 500,
            message = ex.message ?: "Internal Server Error",
            data = null
        )
        return ResponseEntity(body, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleMaxSizeException(exc: MaxUploadSizeExceededException): ResponseEntity<String> {
        return ResponseEntity("File size exceeds the maximum limit!", HttpStatus.PAYLOAD_TOO_LARGE)
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = [NotFoundException::class])
    fun handleNotFoundException(notFoundException: NotFoundException): ResponseEntity<FormatResponse.Error<Nothing>> {
        val body = FormatResponse.Error(
            code = 404,
            status = "not found",
            message = "${notFoundException.message}",
            data = null
        )
        return ResponseEntity(body, HttpStatus.NOT_FOUND)
    }

}