package io.craigmiller160.alternatequeryexperiment.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import io.craigmiller160.alternatequeryexperiment.web.type.EmployeeDTO
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
class QueryDslControllerTest {
  @Autowired private lateinit var mockMvc: MockMvc
  @Autowired private lateinit var objectMapper: ObjectMapper

  @Test
  fun getAllEmployees() {
    val responseString =
      mockMvc
        .get("/querydsl/employees")
        .andExpect { status { isOk() } }
        .andReturn()
        .response
        .contentAsString
    val type = jacksonTypeRef<List<EmployeeDTO>>()
    val employees = objectMapper.readValue(responseString, type)
    println(employees)
  }
}
