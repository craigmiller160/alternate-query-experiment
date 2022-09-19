package io.craigmiller160.alternatequeryexperiment.data.repository

import io.craigmiller160.alternatequeryexperiment.data.entity.Position
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface PositionRepository : JpaRepository<Position, UUID>
