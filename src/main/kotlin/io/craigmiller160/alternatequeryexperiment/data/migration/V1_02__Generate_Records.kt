package io.craigmiller160.alternatequeryexperiment.data.migration

import com.github.javafaker.Faker
import java.time.LocalDate
import java.util.UUID
import kotlin.streams.asSequence
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.SingleConnectionDataSource

class V1_02__Generate_Records : BaseJavaMigration() {
  companion object {
    private const val SELECT_POSITIONS =
      """
            SELECT *
            FROM positions
        """
    private const val INSERT_EMPLOYEE =
      """
            INSERT INTO employees(id, first_name, last_name, date_of_birth, position_id)
            VALUES (:id, :firstName, :lastName, :dateOfBirth, :positionId)
        """

    private const val INSERT_TEAM =
      """
          INSERT INTO teams (id, supervisor_id)
          VALUES (:id, :supervisorId)
      """

    private const val INSERT_TEAM_MEMBER =
      """
          INSERT INTO team_members (id, team_id, employee_id)
          VALUES (:id, :teamId, :employeeId)
      """
  }

  private val faker = Faker()

  override fun migrate(context: Context) {
    val jdbcTemplate =
      NamedParameterJdbcTemplate(SingleConnectionDataSource(context.connection, true))
    val positions = getPositions(jdbcTemplate)
    (0..5).forEach { index -> createTeam(jdbcTemplate, positions) }
  }

  private fun createTeam(jdbcTemplate: NamedParameterJdbcTemplate, positions: Positions) {
    val supervisorId = createEmployee(jdbcTemplate, positions, true)
    val teamId = createTeam(jdbcTemplate, supervisorId)
    (0..10).forEach { index -> createTeamMember(jdbcTemplate, teamId, positions) }
  }

  private fun createTeamMember(
    jdbcTemplate: NamedParameterJdbcTemplate,
    teamId: UUID,
    positions: Positions
  ): UUID {
    val id = UUID.randomUUID()
    val employeeId = createEmployee(jdbcTemplate, positions, false)
    val params =
      MapSqlParameterSource()
        .addValue("id", id)
        .addValue("teamId", teamId)
        .addValue("employeeId", employeeId)
    jdbcTemplate.update(INSERT_TEAM_MEMBER, params)
    return id
  }

  private fun createTeam(jdbcTemplate: NamedParameterJdbcTemplate, supervisorId: UUID): UUID {
    val id = UUID.randomUUID()
    val params = MapSqlParameterSource().addValue("id", id).addValue("supervisorId", supervisorId)
    jdbcTemplate.update(INSERT_TEAM, params)
    return id
  }

  private fun createEmployee(
    jdbcTemplate: NamedParameterJdbcTemplate,
    positions: Positions,
    isSupervisor: Boolean
  ): UUID {
    val id = UUID.randomUUID()
    val positionId = if (isSupervisor) positions.supervisorId else positions.workerId
    val params =
      MapSqlParameterSource()
        .addValue("id", id)
        .addValue("firstName", faker.name().firstName())
        .addValue("lastName", faker.name().lastName())
        .addValue("positionId", positionId)
        .addValue("dateOfBirth", LocalDate.from(faker.date().birthday().toInstant()))

    jdbcTemplate.update(INSERT_EMPLOYEE, params)
    return id
  }

  private fun getPositions(jdbcTemplate: NamedParameterJdbcTemplate): Positions {
    val nullableResult =
      jdbcTemplate
        .queryForStream(SELECT_POSITIONS, MapSqlParameterSource()) { rs, i ->
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
