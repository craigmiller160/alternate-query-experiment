package io.craigmiller160.alternatequeryexperiment.data.entity

import java.time.LocalDate
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "employees")
class Employee {
  @Id var id: UUID = UUID.randomUUID()
  var firstName: String? = null
  var lastName: String? = null
  var dateOfBirth: LocalDate? = null
  lateinit var positionId: UUID
}
