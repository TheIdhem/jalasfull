package ir.faj.jalas.jalas.entities.repository

import ir.faj.jalas.jalas.entities.EventLog
import ir.faj.jalas.jalas.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query

interface EventLogRepository : JpaRepository<EventLog, Int>, JpaSpecificationExecutor<EventLog>{
    @Query("select e from EventLog e where e.checked = false")
    fun findAllByChecked(): List<EventLog>
}