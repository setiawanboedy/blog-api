package selapang.restful.tafakkur.com.model

import jakarta.persistence.*
import selapang.restful.tafakkur.com.dto.UserResponse

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
    var profilePicture: String? = null

){
    fun toUserResponse(): UserResponse{
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