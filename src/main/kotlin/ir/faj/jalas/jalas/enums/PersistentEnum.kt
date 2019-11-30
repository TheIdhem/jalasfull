package ir.faj.jalas.jalas.enums

import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.usertype.UserType
import java.io.Serializable
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

interface PersistentEnum {
    fun getCode(): Byte
}

abstract class PersistentEnumUserType<T : PersistentEnum>(val clazz: Class<T>) : UserType {

    override fun assemble(cached: Serializable, owner: Any?): Any {
        return cached
    }

    override fun deepCopy(value: Any?): Any? {
        return value
    }

    override fun disassemble(value: Any?): Serializable? {
        return value as Serializable
    }

    override fun equals(x: Any?, y: Any?): Boolean {
        return x == y
    }

    override fun hashCode(x: Any?): Int {
        return x?.hashCode() ?: 0
    }

    override fun isMutable(): Boolean {
        return false
    }

    override fun nullSafeGet(rs: ResultSet, names: Array<String>, session: SharedSessionContractImplementor, owner: Any?): Any? {
        val id = rs.getByte(names[0])
        if (rs.wasNull()) {
            return null
        }
        for (value in clazz.enumConstants) {
            if (id == value.getCode()) {
                return value
            }
        }
        throw IllegalStateException("Unknown " + returnedClass().simpleName + " id: $id")
    }

    override fun nullSafeSet(st: PreparedStatement, value: Any?, index: Int, session: SharedSessionContractImplementor) {
        if (value == null) {
            st.setNull(index, Types.TINYINT)
        } else {
            st.setByte(index, (value as PersistentEnum).getCode())
        }
    }

    override fun replace(original: Any?, target: Any?, owner: Any?): Any? {
        return original
    }

    override fun returnedClass(): Class<T> = clazz

    override fun sqlTypes(): IntArray {
        return intArrayOf(Types.TINYINT)
    }

}