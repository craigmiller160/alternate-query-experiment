package io.craigmiller160.alternatequeryexperiment.web.controller

import io.craigmiller160.alternatequeryexperiment.querydsl.QueryDslService
import io.craigmiller160.alternatequeryexperiment.web.type.GetEmployeeDTO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/querydsl")
class QueryDslController(private val queryDslService: QueryDslService) {
  @GetMapping("/employees")
  fun getAllEmployees(): List<GetEmployeeDTO> = queryDslService.getAllEmployees()
}
