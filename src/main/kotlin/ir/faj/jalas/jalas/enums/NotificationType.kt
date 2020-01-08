package ir.faj.jalas.jalas.enums

enum class NotificationType(private val _code: Byte) : PersistentEnum {
    addOrRemoveToPoll(1), voteToOption(2), addOrRemoveUser(3), addOrRemoveOption(4), reservationSession(5),deletePoll(6);

    override fun getCode() = _code
}

class NotificationTypeClassTypeEnum : PersistentEnumUserType<NotificationType>(NotificationType::class.java)