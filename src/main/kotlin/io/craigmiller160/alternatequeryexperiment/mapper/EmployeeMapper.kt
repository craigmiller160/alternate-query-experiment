package io.craigmiller160.alternatequeryexperiment.mapper

import io.craigmiller160.alternatequeryexperiment.data.querydsl.projection.GetEmployeeProjection
import io.craigmiller160.alternatequeryexperiment.web.type.GetEmployeeDTO
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface EmployeeMapper {
  fun queryDslGetEmployeeProjectionListToDto(
    employeeProjection: List<GetEmployeeProjection>
  ): List<GetEmployeeDTO>
}
