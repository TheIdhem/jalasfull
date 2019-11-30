package ir.faj.jalas.jalas.enums

enum class SessionStatus(private val _code: Byte) : PersistentEnum {
    unavailble(0), pending(1), successReserved(2);

    override fun getCode() = _code
}

class SessionStatusClassTypeEnum : PersistentEnumUserType<SessionStatus>(SessionStatus::class.java)