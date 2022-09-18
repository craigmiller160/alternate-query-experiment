package io.craigmiller160.alternatequeryexperiment.web.controller

import io.craigmiller160.alternatequeryexperiment.web.type.EmployeeDTO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/querydsl")
class QueryDslController {
  @GetMapping("/employees")
  fun getAllEmployees(): List<EmployeeDTO> {
    TODO()
  }
}
