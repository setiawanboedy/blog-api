package blog.restful.tafakkur.com.model

import jakarta.persistence.*
import blog.restful.tafakkur.com.dto.response.UserResponse
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "Users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val password: String,

    var phoneNumber: String? = null,
    var address: String? = null,
    var profilePicture: String? = null,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: LocalDateTime? = null,

){

    fun toUserResponse(): UserResponse {
        return UserResponse(
            id = this.id,
            username = this.username,
            email = this.email,
            phoneNumber = this.phoneNumber,
            address = this.address,
            profilePicture = this.profilePicture
        )
    }
}