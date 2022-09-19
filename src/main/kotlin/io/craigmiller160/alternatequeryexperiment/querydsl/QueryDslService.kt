package io.craigmiller160.alternatequeryexperiment.querydsl

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
    queryFactory
      .select(QEmployee.employee, QPosition.position.name)
      .from(QEmployee.employee, QPosition.position)
      .where(QPosition.position.id.eq(QEmployee.employee.positionId))
    TODO()
  }
}
