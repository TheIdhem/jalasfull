package ir.faj.jalas.jalas.entities.repository

import ir.faj.jalas.jalas.entities.Vote
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface VoteRepository : JpaRepository<Vote, Int>, JpaSpecificationExecutor<Vote> {

    fun findByIdIn(votes: List<Int>): List<Vote>

    @Modifying
    @Query("delete from Vote v where v.option.id in ?1")
    fun deleteVotesByOptionId(optionIds: List<Int>)
}