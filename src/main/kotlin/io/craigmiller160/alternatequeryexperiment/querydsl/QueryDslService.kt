package io.craigmiller160.alternatequeryexperiment.querydsl

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import io.craigmiller160.alternatequeryexperiment.data.entity.QEmployee
import io.craigmiller160.alternatequeryexperiment.data.entity.QPosition
import io.craigmiller160.alternatequeryexperiment.web.type.EmployeeDTO
import javax.persistence.EntityManager
import org.springframework.stereotype.Service

@Service
class QueryDslService(
  private val entityManager: EntityManager,
  private val queryFactory: JPAQueryFactory
) {
  // TODO add pagination
  fun getAllEmployees(): List<EmployeeDTO> {
    // TODO try to find some way to use the constructor of the DTO directly
    return queryFactory
      .query()
      .select(
        Projections.constructor(
          EmployeeDTO::class.java,
          QEmployee.employee.id,
          QEmployee.employee.firstName,
          QEmployee.employee.lastName,
          QEmployee.employee.dateOfBirth,
          QEmployee.employee.positionId,
          QPosition.position.name))
      .from(QEmployee.employee, QPosition.position)
      .where(QPosition.position.id.eq(QEmployee.employee.positionId))
      .fetch()
  }
}
