package ir.faj.jalas.jalas.dto.rdbms

import ir.faj.jalas.jalas.entities.Notification
import ir.faj.jalas.jalas.enums.NotificationType


open class NotificationBaseDto : JalasDto<Notification> {
    var id: Int = 0
    var user: UserShallowDto? = null
    var type: NotificationType? = null


    override fun toEntity(entity: Notification?): Notification {
        val model = Notification()
        model.id = id
        model.user = user?.toEntity() ?: model.user
        model.type = type ?: model.type
        return model
    }


}

class NotificationShallowDto : NotificationBaseDto()