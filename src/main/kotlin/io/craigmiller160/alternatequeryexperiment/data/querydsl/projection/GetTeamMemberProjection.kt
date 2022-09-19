package io.craigmiller160.alternatequeryexperiment.data.querydsl.projection

import com.querydsl.core.annotations.QueryProjection
import java.util.UUID

data class GetTeamMemberProjection
@QueryProjection
constructor(val id: UUID, val firstName: String, val lastName: String, val positionName: String)
