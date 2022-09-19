package io.craigmiller160.alternatequeryexperiment.data.querydsl.projection

import com.querydsl.core.annotations.QueryProjection
import java.util.UUID

data class GetTeamProjection
@QueryProjection
constructor(
  val id: UUID,
  val supervisorId: UUID,
  val supervisorFirstName: String,
  val supervisorLastName: String
)
