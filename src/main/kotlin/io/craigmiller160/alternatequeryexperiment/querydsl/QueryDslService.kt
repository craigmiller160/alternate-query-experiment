package io.craigmiller160.alternatequeryexperiment.querydsl

import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import io.craigmiller160.alternatequeryexperiment.data.entity.Employee
import io.craigmiller160.alternatequeryexperiment.data.entity.QEmployee
import io.craigmiller160.alternatequeryexperiment.data.entity.QPosition
import io.craigmiller160.alternatequeryexperiment.data.entity.QTeam
import io.craigmiller160.alternatequeryexperiment.data.entity.QTeamMember
import io.craigmiller160.alternatequeryexperiment.data.querydsl.projection.QGetEmployeeProjection
import io.craigmiller160.alternatequeryexperiment.data.querydsl.projection.QGetTeamMemberProjection
import io.craigmiller160.alternatequeryexperiment.data.querydsl.projection.QGetTeamProjection
import io.craigmiller160.alternatequeryexperiment.mapper.EmployeeMapper
import io.craigmiller160.alternatequeryexperiment.mapper.TeamMapper
import io.craigmiller160.alternatequeryexperiment.web.type.GetEmployeeDTO
import io.craigmiller160.alternatequeryexperiment.web.type.GetTeamDTO
import io.craigmiller160.alternatequeryexperiment.web.type.PageResult
import java.util.UUID
import kotlin.RuntimeException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class QueryDslService(
  private val queryFactory: JPAQueryFactory,
  private val employeeMapper: EmployeeMapper,
  private val queryDslSupport: QueryDslSupport,
  private val teamMapper: TeamMapper
) {

  private fun buildSearchWhereClause(firstNameStartsWith: String?): BooleanBuilder {
    val builder = BooleanBuilder(QPosition.position.id.eq(QEmployee.employee.positionId))
    return firstNameStartsWith?.let { builder.and(QEmployee.employee.firstName.startsWith(it)) }
      ?: builder
  }
  fun getAllEmployees(
    page: Int,
    size: Int,
    firstNameStartsWith: String?
  ): PageResult<GetEmployeeDTO> {
    val baseQuery =
      queryFactory
        .query()
        .from(QEmployee.employee)
        .join(QPosition.position)
        .on(QEmployee.employee.positionId.eq(QPosition.position.id))
        .where(buildSearchWhereClause(firstNameStartsWith))
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
    val team =
      queryFactory
        .query()
        .from(QTeam.team)
        .join(QEmployee.employee)
        .on(QTeam.team.supervisorId.eq(QEmployee.employee.id))
        .where(QTeam.team.id.eq(teamId))
        .select(
          QGetTeamProjection(
            QTeam.team.id,
            QTeam.team.supervisorId,
            QEmployee.employee.firstName,
            QEmployee.employee.lastName))
        .fetchOne()
        ?: throw RuntimeException("Not found: $teamId")

    val members =
      queryFactory
        .query()
        .from(QTeamMember.teamMember)
        .join(QEmployee.employee)
        .on(QTeamMember.teamMember.employeeId.eq(QEmployee.employee.id))
        .join(QPosition.position)
        .on(QPosition.position.id.eq(QEmployee.employee.positionId))
        .where(QTeamMember.teamMember.teamId.eq(team.id))
        .orderBy(QEmployee.employee.lastName.asc(), QEmployee.employee.firstName.asc())
        .select(
          QGetTeamMemberProjection(
            QEmployee.employee.id,
            QEmployee.employee.firstName,
            QEmployee.employee.lastName,
            QPosition.position.name))
        .fetch()

    return teamMapper.getTeamProjectionToGetTeamDTO(team, members)
  }
}
