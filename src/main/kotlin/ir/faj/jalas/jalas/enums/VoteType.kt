package ir.faj.jalas.jalas.enums

enum class VoteType(private val _code: Byte) : PersistentEnum {
    up(0), down(1);

    override fun getCode() = _code
}

class VoteTypeClassTypeEnum : PersistentEnumUserType<VoteType>(VoteType::class.java)