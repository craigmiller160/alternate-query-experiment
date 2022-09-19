package io.craigmiller160.alternatequeryexperiment.data.querydsl.projection

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDate
import java.util.UUID

data class GetEmployeeProjection
@QueryProjection
constructor(
  val id: UUID,
  val firstName: String?,
  val lastName: String?,
  val dateOfBirth: LocalDate?,
  val positionId: UUID,
  val positionName: String
)
