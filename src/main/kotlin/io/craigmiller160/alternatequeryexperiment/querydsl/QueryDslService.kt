package io.craigmiller160.alternatequeryexperiment.querydsl

import com.querydsl.jpa.impl.JPAQueryFactory
import io.craigmiller160.alternatequeryexperiment.data.entity.QEmployee
import io.craigmiller160.alternatequeryexperiment.data.entity.QPosition
import io.craigmiller160.alternatequeryexperiment.data.querydsl.projection.QGetEmployeeProjection
import io.craigmiller160.alternatequeryexperiment.mapper.EmployeeMapper
import io.craigmiller160.alternatequeryexperiment.web.type.GetEmployeeDTO
import io.craigmiller160.alternatequeryexperiment.web.type.PageResult
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.support.Querydsl
import org.springframework.stereotype.Service

@Service
class QueryDslService(
  private val queryFactory: JPAQueryFactory,
  private val employeeMapper: EmployeeMapper,
  private val employeeQuerydsl: Querydsl
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
      PageRequest.of(page, size, Sort.by(Sort.Order.asc("firstName"), Sort.Order.asc("lastName")))

    return employeeQuerydsl
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
  }

  //      .orderBy(QEmployee.employee.lastName.asc(), QEmployee.employee.firstName.asc())
  //      .offset(page.toLong() * size.toLong())
  //      .limit(size.toLong())
}
