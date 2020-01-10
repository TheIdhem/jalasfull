package ir.faj.jalas.jalas.entities

import ir.faj.jalas.jalas.dto.rdbms.NotificationShallowDto
import ir.faj.jalas.jalas.enums.NotificationType
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "ja_notification")
@DynamicUpdate
class Notification(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        @Basic
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "creation_time")
        var creationTime: Date = Date(),


        @Basic
        @Column(name = "type")
        @Type(type = "ir.faj.jalas.jalas.enums.NotificationTypeClassTypeEnum")
        var type: NotificationType? = null,


        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "user_id", nullable = false)
        var user: User = User()


) {
    constructor() : this(id = 0)

    fun toShallow(isNested: Boolean = false): NotificationShallowDto {
        val dto = NotificationShallowDto()
        if (!isNested)
            dto.user = user.toShallow(true)
        dto.id = id
        dto.type = type

        return dto
    }
}

