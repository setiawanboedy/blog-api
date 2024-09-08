package blog.restful.tafakkur.com.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil {
    private val secretKey: SecretKey = Keys.hmacShaKeyFor("my_super_secret_key_for_jwt_signing".toByteArray())


    fun generateToken(userDetails: UserDetails): String {
        val claims = Jwts.claims().setSubject(userDetails.username)
        claims["role"] = userDetails.authorities.firstOrNull()?.authority ?: "USER"
        return  createToken(claims, userDetails.username)
    }

    private fun createToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000*60*60*10))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    fun extractExpiresIn(token: String): String {
        val expirationTime = extractExpiration(token).time
        val expirationInstant = Instant.ofEpochMilli(expirationTime)
        val isoFormater = DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC)

        return isoFormater.format(expirationInstant)
    }

    private fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return  claimsResolver(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).body
    }

    private fun isTokenExpired(token: String) : Boolean {
        return extractExpiration(token).before(Date())
    }
}