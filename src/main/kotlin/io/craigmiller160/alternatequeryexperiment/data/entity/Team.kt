package io.craigmiller160.alternatequeryexperiment.data.entity

import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "teams")
class Team {
  @Id var id: UUID = UUID.randomUUID()
  lateinit var supervisorId: UUID
}
