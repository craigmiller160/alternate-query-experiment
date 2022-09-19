package io.craigmiller160.alternatequeryexperiment.querydsl

import com.querydsl.jpa.impl.JPAQuery
import io.craigmiller160.alternatequeryexperiment.data.entity.Employee
import io.craigmiller160.alternatequeryexperiment.data.entity.QEmployee
import io.craigmiller160.alternatequeryexperiment.web.type.EmployeeDTO
import javax.persistence.EntityManager
import org.springframework.stereotype.Service

@Service
class QueryDslService(private val entityManager: EntityManager) {
  fun getAllEmployees(): List<EmployeeDTO> {
    val query = JPAQuery<Employee>(entityManager)
    return query.select(QEmployee.employee).from(QEmployee.employee).fetch().map {
      EmployeeDTO.fromEntities(it)
    }
  }
}
