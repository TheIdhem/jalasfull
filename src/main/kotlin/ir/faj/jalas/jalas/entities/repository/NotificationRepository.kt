package ir.faj.jalas.jalas.entities.repository

import ir.faj.jalas.jalas.entities.Notification
import ir.faj.jalas.jalas.enums.NotificationType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface NotificationRepository : JpaRepository<Notification, Int>, JpaSpecificationExecutor<Notification> {

    fun findByUserIdAndType(userId: Int, type: NotificationType): Notification?

    @Modifying
    @Query("delete from Notification n where n.user.id=?1 and n.type=?2")
    fun deleteByUserIdAndType(userId: Int, type: NotificationType)
}