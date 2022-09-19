package io.craigmiller160.alternatequeryexperiment.querydsl

import com.querydsl.jpa.impl.JPAQueryFactory
import io.craigmiller160.alternatequeryexperiment.data.entity.Employee
import io.craigmiller160.alternatequeryexperiment.data.entity.QEmployee
import io.craigmiller160.alternatequeryexperiment.data.entity.QPosition
import io.craigmiller160.alternatequeryexperiment.data.entity.QTeam
import io.craigmiller160.alternatequeryexperiment.data.querydsl.projection.QGetEmployeeProjection
import io.craigmiller160.alternatequeryexperiment.mapper.EmployeeMapper
import io.craigmiller160.alternatequeryexperiment.web.type.GetEmployeeDTO
import io.craigmiller160.alternatequeryexperiment.web.type.GetTeamDTO
import io.craigmiller160.alternatequeryexperiment.web.type.PageResult
import java.util.UUID
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class QueryDslService(
  private val queryFactory: JPAQueryFactory,
  private val employeeMapper: EmployeeMapper,
  private val queryDslSupport: QueryDslSupport
) {
  fun getAllEmployees(page: Int, size: Int): PageResult<GetEmployeeDTO> {
    val baseQuery =
      queryFactory
        .query()
        .from(QEmployee.employee)
        .join(QPosition.position)
        .on(QEmployee.employee.positionId.eq(QPosition.position.id))
        .where(QPosition.position.id.eq(QEmployee.employee.positionId))
    val count = baseQuery.select(QEmployee.employee.id.count()).fetchFirst()

    val pageable =
      PageRequest.of(page, size, Sort.by(Sort.Order.asc("lastName"), Sort.Order.asc("firstName")))

    return queryDslSupport
      .newQuerydsl(Employee::class.java)
      .applyPagination(pageable, baseQuery)
      .select(
        QGetEmployeeProjection(
          QEmployee.employee.id,
          QEmployee.employee.firstName,
          QEmployee.employee.lastName,
          QEmployee.employee.dateOfBirth,
          QEmployee.employee.positionId,
          QPosition.position.name))
      .fetch()
      .let { employeeMapper.queryDslGetEmployeeProjectionListToDto(it) }
      .let { PageResult(it, count) }

    //      .orderBy(QEmployee.employee.lastName.asc(), QEmployee.employee.firstName.asc())
    //      .offset(page.toLong() * size.toLong())
    //      .limit(size.toLong())
  }

  fun getTeam(teamId: UUID): GetTeamDTO {
    val tuple =
      queryFactory
        .query()
        .from(QTeam.team)
        .join(QEmployee.employee)
        .on(QTeam.team.supervisorId.eq(QEmployee.employee.id))
        .where(QTeam.team.id.eq(teamId))
        .select(QTeam.team, QEmployee.employee)
        .fetchOne()
    TODO()
  }
}
