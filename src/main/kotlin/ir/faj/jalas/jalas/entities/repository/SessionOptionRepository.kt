package ir.faj.jalas.jalas.entities.repository

import ir.faj.jalas.jalas.entities.SessionOption
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface SessionOptionRepository : JpaRepository<SessionOption, Int>, JpaSpecificationExecutor<SessionOption>