package ir.faj.jalas.jalas.dto.rdbms

interface JalasDto<R> {

    fun toEntity(entity: R? = null): R

}