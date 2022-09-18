package io.craigmiller160.alternatequeryexperiment.data

import java.util.UUID
import javax.annotation.PostConstruct
import kotlin.streams.asSequence
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class GenerateData(private val jdbcTemplate: JdbcTemplate) {
  companion object {
    private const val SELECT_POSITIONS =
      """
            SELECT *
            FROM positions
        """
    private const val INSERT_EMPLOYEE =
      """
            INSERT INTO employees(id, first_name, last_name, date_of_birth, position_id)
        """
  }
  @PostConstruct fun generate() {}

  private fun getPositions(): Positions {
    val nullableResult =
      jdbcTemplate
        .queryForStream(SELECT_POSITIONS) { rs, i ->
          val id = UUID.fromString(rs.getString("id"))
          if (rs.getString("name") == "Supervisor") {
            NullablePositions(id, null)
          } else {
            NullablePositions(null, id)
          }
        }
        .asSequence()
        .reduce { acc, item ->
          acc.copy(
            supervisorId = item.supervisorId ?: acc.supervisorId,
            workerId = item.workerId ?: acc.workerId)
        }
    return Positions.fromNullable(nullableResult)
  }

  private data class NullablePositions(val supervisorId: UUID?, val workerId: UUID?)

  private data class Positions(val supervisorId: UUID, val workerId: UUID) {
    companion object {
      fun fromNullable(nullable: NullablePositions): Positions =
        Positions(supervisorId = nullable.supervisorId!!, workerId = nullable.workerId!!)
    }
  }
}
