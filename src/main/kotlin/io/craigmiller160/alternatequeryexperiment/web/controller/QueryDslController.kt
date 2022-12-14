package io.craigmiller160.alternatequeryexperiment.web.controller

import io.craigmiller160.alternatequeryexperiment.querydsl.QueryDslService
import io.craigmiller160.alternatequeryexperiment.web.type.GetEmployeeDTO
import io.craigmiller160.alternatequeryexperiment.web.type.GetTeamDTO
import io.craigmiller160.alternatequeryexperiment.web.type.PageResult
import java.util.UUID
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/querydsl")
class QueryDslController(private val queryDslService: QueryDslService) {
  @GetMapping("/employees")
  fun getAllEmployees(
    @RequestParam("page") page: Int,
    @RequestParam("size") size: Int,
    @RequestParam(value = "firstNameStartsWith", required = false) firstNameStartsWith: String?
  ): PageResult<GetEmployeeDTO> = queryDslService.getAllEmployees(page, size, firstNameStartsWith)

  @GetMapping("/teams/{teamId}")
  fun getTeam(@PathVariable("teamId") teamId: UUID): GetTeamDTO = queryDslService.getTeam(teamId)
}
