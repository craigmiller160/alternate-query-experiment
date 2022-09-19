package io.craigmiller160.alternatequeryexperiment.config

import com.querydsl.core.types.dsl.PathBuilderFactory
import com.querydsl.jpa.impl.JPAQueryFactory
import io.craigmiller160.alternatequeryexperiment.data.entity.Employee
import javax.persistence.EntityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.support.Querydsl

@Configuration
class QueryDSLConfig(private val entityManager: EntityManager) {
  @Bean fun queryFactory(): JPAQueryFactory = JPAQueryFactory(entityManager)

  @Bean
  fun employeeQuerydsl(): Querydsl =
    Querydsl(entityManager, PathBuilderFactory().create(Employee::class.java))
}
