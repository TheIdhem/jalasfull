package ir.faj.jalas.jalas.entities.repository

import ir.faj.jalas.jalas.entities.SessionOption
import ir.faj.jalas.jalas.entities.User
import ir.faj.jalas.jalas.entities.Vote
import ir.faj.jalas.jalas.enums.VoteType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface VoteRepository : JpaRepository<Vote, Int>, JpaSpecificationExecutor<Vote> {

    fun findByUserAndOptionAndStatus(user: User, option: SessionOption, status: VoteType): Vote?

    @Modifying
    @Query("delete from Vote v where v.option.id in ?1")
    fun deleteVotesByOptionId(optionIds: List<Int>)

    @Modifying
    @Query("delete from Vote v where v.option.id in ?1 and v.user.id=?2")
    fun deleteVotesByOptionIdAndUserId(optionIds: List<Int>, userId: Int)
}