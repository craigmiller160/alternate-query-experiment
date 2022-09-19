package io.craigmiller160.alternatequeryexperiment.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import io.craigmiller160.alternatequeryexperiment.data.entity.Employee
import io.craigmiller160.alternatequeryexperiment.data.repository.EmployeeRepository
import io.craigmiller160.alternatequeryexperiment.data.repository.PositionRepository
import io.craigmiller160.alternatequeryexperiment.web.type.EmployeeDTO
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
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
  @Autowired private lateinit var employeeRepository: EmployeeRepository
  @Autowired private lateinit var positionRepository: PositionRepository

  private lateinit var positionMap: Map<UUID, String>
  private lateinit var employees: List<Employee>

  @BeforeEach
  fun setup() {
    positionMap =
      positionRepository.findAll().associate { position -> position.id to position.name }
    employees =
      employeeRepository.findAll().sortedWith { emp1, emp2 ->
        val lastNameCompare = emp1.lastName?.compareTo(emp2?.lastName ?: "") ?: 1
        if (lastNameCompare == 0) {
          emp1.firstName?.compareTo(emp2?.firstName ?: "") ?: 1
        } else {
          lastNameCompare
        }
      }
  }

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
    assertThat(employees).hasSize(this.employees.size)
    employees.forEachIndexed { index, employee ->
      val expected = this.employees[index]
      assertThat(employee)
        .hasFieldOrPropertyWithValue("id", expected.id)
        .hasFieldOrPropertyWithValue("firstName", expected.firstName)
        .hasFieldOrPropertyWithValue("lastName", expected.lastName)
        .hasFieldOrPropertyWithValue("dateOfBirth", expected.dateOfBirth)
        .hasFieldOrPropertyWithValue("positionId", expected.positionId)
        .hasFieldOrPropertyWithValue("positionName", positionMap[expected.positionId])
    }
  }
}
