package io.craigmiller160.alternatequeryexperiment.data.repository

import io.craigmiller160.alternatequeryexperiment.data.entity.Team
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface TeamRepository : JpaRepository<Team, UUID>
