package io.craigmiller160.alternatequeryexperiment.querydsl

import com.querydsl.core.types.dsl.PathBuilderFactory
import javax.persistence.EntityManager
import org.springframework.data.jpa.repository.support.Querydsl
import org.springframework.stereotype.Component

@Component
class QueryDslSupport(private val entityManager: EntityManager) {
  fun newQuerydsl(type: Class<*>): Querydsl =
    Querydsl(entityManager, PathBuilderFactory().create(type))
}
