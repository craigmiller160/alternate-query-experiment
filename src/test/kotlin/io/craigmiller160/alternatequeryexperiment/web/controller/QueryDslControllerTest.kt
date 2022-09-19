package io.craigmiller160.alternatequeryexperiment.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import io.craigmiller160.alternatequeryexperiment.data.entity.Employee
import io.craigmiller160.alternatequeryexperiment.data.entity.Team
import io.craigmiller160.alternatequeryexperiment.data.entity.TeamMember
import io.craigmiller160.alternatequeryexperiment.data.repository.EmployeeRepository
import io.craigmiller160.alternatequeryexperiment.data.repository.PositionRepository
import io.craigmiller160.alternatequeryexperiment.data.repository.TeamMemberRepository
import io.craigmiller160.alternatequeryexperiment.data.repository.TeamRepository
import io.craigmiller160.alternatequeryexperiment.web.type.GetEmployeeDTO
import io.craigmiller160.alternatequeryexperiment.web.type.PageResult
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
  @Autowired private lateinit var teamRepository: TeamRepository
  @Autowired private lateinit var teamMemberRepository: TeamMemberRepository

  private lateinit var positionMap: Map<UUID, String>
  private lateinit var employees: List<Employee>
  private lateinit var teams: List<Team>
  private lateinit var teamMembers: List<TeamMember>

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
    teams = teamRepository.findAll()
    teamMembers = teamMemberRepository.findAll()
  }

  @Test
  fun getAllEmployees() {
    val responseString =
      mockMvc
        .get("/querydsl/employees?page=1&size=10")
        .andExpect { status { isOk() } }
        .andReturn()
        .response
        .contentAsString
    val type = jacksonTypeRef<PageResult<GetEmployeeDTO>>()
    val employees = objectMapper.readValue(responseString, type)
    val expectedEmployees = this.employees.filterIndexed { index, employee -> index in 10..19 }

    assertThat(employees.totalRecords).isEqualTo(this.employees.size.toLong())
    assertThat(employees.contents).hasSize(expectedEmployees.size)
    employees.contents.forEachIndexed { index, employee ->
      val expected = expectedEmployees[index]
      assertThat(employee)
        .hasFieldOrPropertyWithValue("id", expected.id)
        .hasFieldOrPropertyWithValue("firstName", expected.firstName)
        .hasFieldOrPropertyWithValue("lastName", expected.lastName)
        .hasFieldOrPropertyWithValue("dateOfBirth", expected.dateOfBirth)
        .hasFieldOrPropertyWithValue("positionId", expected.positionId)
        .hasFieldOrPropertyWithValue("positionName", positionMap[expected.positionId])
    }
  }

  @Test fun getTeam() {}
}
