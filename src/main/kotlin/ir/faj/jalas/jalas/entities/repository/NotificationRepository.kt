package ir.faj.jalas.jalas.entities.repository

import ir.faj.jalas.jalas.entities.Notification
import ir.faj.jalas.jalas.enums.NotificationType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface NotificationRepository : JpaRepository<Notification, Int>, JpaSpecificationExecutor<Notification> {

    fun findByUserIdAndType(userId: Int, type: NotificationType): Notification?

    fun deleteByUserIdAndType(userId: Int, type: NotificationType)
}