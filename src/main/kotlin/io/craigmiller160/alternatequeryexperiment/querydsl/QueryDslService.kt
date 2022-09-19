package io.craigmiller160.alternatequeryexperiment.querydsl

import com.querydsl.jpa.impl.JPAQueryFactory
import io.craigmiller160.alternatequeryexperiment.data.entity.QEmployee
import io.craigmiller160.alternatequeryexperiment.data.entity.QPosition
import io.craigmiller160.alternatequeryexperiment.data.querydsl.projection.QGetEmployeeProjection
import io.craigmiller160.alternatequeryexperiment.web.type.GetEmployeeDTO
import org.springframework.stereotype.Service

@Service
class QueryDslService(private val queryFactory: JPAQueryFactory) {
  // TODO add pagination
  fun getAllEmployees(): List<GetEmployeeDTO> {
    //    PathBuilderFactory().create()
    //    Querydsl(null, null)
    return queryFactory
      .query()
      .select(
        QGetEmployeeProjection(
          QEmployee.employee.id,
          QEmployee.employee.firstName,
          QEmployee.employee.lastName,
          QEmployee.employee.dateOfBirth,
          QEmployee.employee.positionId,
          QPosition.position.name))
      .from(QEmployee.employee, QPosition.position)
      .where(QPosition.position.id.eq(QEmployee.employee.positionId))
      .orderBy(QEmployee.employee.lastName.asc(), QEmployee.employee.firstName.asc())
      .fetch()
  }
}
