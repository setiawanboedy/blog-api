package blog.restful.tafakkur.com.exception

class BadCredentialsException(message: String): RuntimeException(message)

class UnauthorizedException(message: String) : RuntimeException(message)