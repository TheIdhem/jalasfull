package ir.faj.jalas.jalas.entities.repository

import ir.faj.jalas.jalas.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<User, Int>, JpaSpecificationExecutor<User> {
    @Query("select u from User u where u.username = ?1")
    fun findByUsername(username: String): User
}