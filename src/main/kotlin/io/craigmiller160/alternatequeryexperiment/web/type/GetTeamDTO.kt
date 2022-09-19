package io.craigmiller160.alternatequeryexperiment.web.type

import java.util.UUID

data class GetTeamMemberDTO(
  val id: UUID,
  val firstName: String,
  val lastName: String,
  val positionName: String
)

data class GetTeamDTO(
  val id: UUID,
  val supervisorId: UUID,
  val supervisorFirstName: String,
  val supervisorLastName: String
)
