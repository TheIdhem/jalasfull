package ir.faj.jalas.jalas.controllers.model

import io.jsonwebtoken.Jwt
import ir.faj.jalas.jalas.dto.rdbms.UserShallowDto
import java.io.Serializable

class JwtResponse(val token: String, val user: UserShallowDto) : Serializable {
    companion object {

        private const val serialVersionUID = -8091879091924046844L
    }
}