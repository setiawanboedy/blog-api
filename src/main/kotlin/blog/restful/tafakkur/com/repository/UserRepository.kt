package blog.restful.tafakkur.com.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import blog.restful.tafakkur.com.model.User

@Repository
interface UserRepository : JpaRepository<User, Long>{
    fun findUserByUsername(username: String): User?
    fun findByEmail(email: String): User?
}