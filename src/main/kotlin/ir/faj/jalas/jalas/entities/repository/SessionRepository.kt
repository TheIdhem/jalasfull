package ir.faj.jalas.jalas.entities.repository

import ir.faj.jalas.jalas.entities.Session
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface SessionRepository : JpaRepository<Session, Int>, JpaSpecificationExecutor<Session> {

}