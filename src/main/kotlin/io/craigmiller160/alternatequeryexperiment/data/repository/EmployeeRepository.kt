package io.craigmiller160.alternatequeryexperiment.data.repository

import io.craigmiller160.alternatequeryexperiment.data.entity.Employee
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface EmployeeRepository : JpaRepository<Employee, UUID>
