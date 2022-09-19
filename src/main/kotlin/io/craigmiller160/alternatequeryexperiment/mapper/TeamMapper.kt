package io.craigmiller160.alternatequeryexperiment.mapper

import io.craigmiller160.alternatequeryexperiment.data.querydsl.projection.GetTeamMemberProjection
import io.craigmiller160.alternatequeryexperiment.data.querydsl.projection.GetTeamProjection
import io.craigmiller160.alternatequeryexperiment.web.type.GetTeamDTO
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface TeamMapper {
  @Mapping(source = "team.id", target = "id")
  @Mapping(source = "team.supervisorId", target = "supervisorId")
  @Mapping(source = "team.supervisorFirstName", target = "supervisorFirstName")
  @Mapping(source = "team.supervisorLastName", target = "supervisorLastName")
  @Mapping(source = "members", target = "members")
  fun getTeamProjectionToGetTeamDTO(
    team: GetTeamProjection,
    members: List<GetTeamMemberProjection>
  ): GetTeamDTO
}
