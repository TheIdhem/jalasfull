package ir.faj.jalas.jalas.entities.repository

import ir.faj.jalas.jalas.entities.EventLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface EventLogRepository : JpaRepository<EventLog, Int>, JpaSpecificationExecutor<EventLog>