package ir.faj.jalas.jalas.service

import ir.faj.jalas.jalas.JalasApplication
import ir.faj.jalas.jalas.entities.repository.UserRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import ir.faj.jalas.jalas.controllers.model.AvailableRoomRequest
import ir.faj.jalas.jalas.entities.User
import ir.faj.jalas.jalas.utility.addHours
import org.junit.Before
import org.mockito.Mockito
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import java.util.*


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [JalasApplication::class])
@AutoConfigureMockMvc
@TestPropertySource(locations = ["classpath:application-test.properties"])
class ServiceTests {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var userRepository: UserRepository

    private val mapper = ObjectMapper()

    @Before
    fun init(){
        Mockito.`when`(userRepository.findAll()).thenReturn(listOf(User(username="the_idhem",email = "mohammadf00900@gmail.com")))
    }

    @Test
    fun contextLoads() {
        Mockito.`when`(userRepository.findAll()).thenReturn(listOf(User(username="the_idhem",email = "mohammadf00900@gmail.com")))

    }

    @Test
    fun roomApiTest(){
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false)
        val ow = mapper.writer().withDefaultPrettyPrinter()
        val requestJson = ow.writeValueAsString(AvailableRoomRequest(startAt = Date(), endAt = Date().addHours(4)))
        val mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1.0/session")
                .header("Origin", "*")
                .param("username","the_idhem")
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn()
        println("salaaaaaaaaaaaaam:::::::::::::::::")
        println(mvcResult.response)
    }


}
