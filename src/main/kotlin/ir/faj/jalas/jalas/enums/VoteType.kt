package ir.faj.jalas.jalas.enums

enum class VoteType(private val _code: Byte) : PersistentEnum {
    up(1), down(2), delete(3), soso(4);

    override fun getCode() = _code
}

class VoteTypeClassTypeEnum : PersistentEnumUserType<VoteType>(VoteType::class.java)