package io.craigmiller160.alternatequeryexperiment.mapper

import io.craigmiller160.alternatequeryexperiment.data.querydsl.projection.GetTeamProjection
import io.craigmiller160.alternatequeryexperiment.web.type.GetTeamDTO
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface TeamMapper {
  fun getTeamProjectionToGetTeamDTO(projection: GetTeamProjection): GetTeamDTO
}
