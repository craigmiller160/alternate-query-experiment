package io.craigmiller160.alternatequeryexperiment.mapper

import io.craigmiller160.alternatequeryexperiment.data.querydsl.projection.GetTeamProjection
import io.craigmiller160.alternatequeryexperiment.web.type.GetTeamDTO
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface TeamMapper {
  //  @Mapping(target = "members", defaultExpression = "java(List.of())")
  fun getTeamProjectionToGetTeamDTO(projection: GetTeamProjection): GetTeamDTO
}
