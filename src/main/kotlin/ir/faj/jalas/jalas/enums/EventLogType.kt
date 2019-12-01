package ir.faj.jalas.jalas.enums

enum class EventLogType(private val _code: Byte) : PersistentEnum {
    shouldRequestAgain(0), roomNotAvailable(1),passed(2);

    override fun getCode() = _code
}

class EventLogTypeClassTypeEnum : PersistentEnumUserType<EventLogType>(EventLogType::class.java)