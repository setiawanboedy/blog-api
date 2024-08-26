package blog.restful.tafakkur.com.service

import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class TokenBlacklistService {
    private val blacklist = mutableSetOf<String>()

    fun addToken(token: String){
        blacklist.add(token)
    }

    fun isTokenBlacklisted(token: String): Boolean{
        return blacklist.contains(token)
    }
}