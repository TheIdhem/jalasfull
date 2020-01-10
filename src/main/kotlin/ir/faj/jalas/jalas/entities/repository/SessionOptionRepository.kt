package ir.faj.jalas.jalas.entities.repository

import ir.faj.jalas.jalas.entities.SessionOption
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface SessionOptionRepository : JpaRepository<SessionOption, Int>, JpaSpecificationExecutor<SessionOption> {
    @Modifying
    @Query("delete from SessionOption s where s.id in ?1")
    fun deleteOptionsById(optionIds: List<Int>)

    @Modifying
    @Query("delete from SessionOption s where s.id = ?1")
    fun deleteOptionById(optionId: Int)

    fun findBySessionId(sessionId:Int):List<SessionOption>
}