package selapang.restful.tafakkur.com.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import blog.restful.tafakkur.com.dto.request.AuthenticationRequest
import blog.restful.tafakkur.com.dto.request.RegisterRequest

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `test successful login`(){
        val authRequest = AuthenticationRequest(
            username = "admin",
            password = "password"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.token").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.expiresIn").exists())

    }

    @Test
    fun `test login with invalid credential`(){
        val authenticationRequest = AuthenticationRequest(
            username = "rendi",
            password = "sinaga"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationRequest))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `test successful register`(){
        val registerRequest = RegisterRequest(
            username = "admin2",
            email = "admin2@admin.com",
            password = "password"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `test register with invalid request`(){
        val registerRequest = RegisterRequest(
            username = "admin2",
            email = "admin2@admin.com",
            password = ""
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}