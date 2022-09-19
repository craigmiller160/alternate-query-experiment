package io.craigmiller160.alternatequeryexperiment.data.repository

import io.craigmiller160.alternatequeryexperiment.data.entity.TeamMember
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface TeamMemberRepository : JpaRepository<TeamMember, UUID>
