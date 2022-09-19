package io.craigmiller160.alternatequeryexperiment.web.type

import com.querydsl.core.annotations.QueryProjection
import io.craigmiller160.alternatequeryexperiment.data.entity.Employee
import java.time.LocalDate
import java.util.UUID

data class EmployeeDTO
@QueryProjection
constructor(
  val id: UUID,
  val firstName: String?,
  val lastName: String?,
  val dateOfBirth: LocalDate?,
  val positionId: UUID,
  val positionName: String
) {
  companion object {
    fun fromEntities(employee: Employee): EmployeeDTO =
      EmployeeDTO(
        id = employee.id,
        firstName = employee.firstName,
        lastName = employee.lastName,
        dateOfBirth = employee.dateOfBirth,
        positionId = employee.positionId,
        positionName = "")
  }
}
