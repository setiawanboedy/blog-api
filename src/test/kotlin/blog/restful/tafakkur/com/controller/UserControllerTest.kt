package blog.restful.tafakkur.com.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import blog.restful.tafakkur.com.dto.request.UpdateUserRequest

// @SpringBootTest
// @AutoConfigureMockMvc
// @ActiveProfiles("test")
// class UserControllerTest {
//     @Autowired
//     private lateinit var mockMvc: MockMvc

//     @Autowired
//     private lateinit var objectMapper: ObjectMapper

//     @Test
//     fun `test successful update user`(){
//         val updateUserRequest = UpdateUserRequest(
//             phoneNumber = "098908908",
//             address = "Praya",
//             profilePicture = "www.google.com"
//         )

//         mockMvc.perform(
//             MockMvcRequestBuilders.put("/api/users/me")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(updateUserRequest))
//         )
//             .andExpect(MockMvcResultMatchers.status().isOk)
//             .andExpect(MockMvcResultMatchers.jsonPath("$.data.phoneNumber").value("098908908"))
//     }
// }