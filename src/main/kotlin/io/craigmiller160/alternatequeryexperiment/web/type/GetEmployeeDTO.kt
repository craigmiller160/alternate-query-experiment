package io.craigmiller160.alternatequeryexperiment.web.type

import java.time.LocalDate
import java.util.UUID

data class GetEmployeeDTO
constructor(
  val id: UUID,
  val firstName: String?,
  val lastName: String?,
  val dateOfBirth: LocalDate?,
  val positionId: UUID,
  val positionName: String
)
