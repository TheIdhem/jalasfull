package ir.faj.jalas.jalas.entities.repository

import ir.faj.jalas.jalas.entities.EventLog
import ir.faj.jalas.jalas.entities.Session
import ir.faj.jalas.jalas.enums.EventLogType
import ir.faj.jalas.jalas.enums.SessionStatus
import ir.faj.jalas.jalas.enums.SessionStatusClassTypeEnum
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query

interface SessionRepository : JpaRepository<Session, Int>, JpaSpecificationExecutor<Session> {
    @Query("select e from Session e where e.status=?1")
    fun findAllByStatus(status: SessionStatus): List<Session>
}