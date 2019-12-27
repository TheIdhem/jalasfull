package ir.faj.jalas.jalas.controllers.model

import javax.validation.constraints.Email

data class UserRegisterRequest(val username: String, val password: String, val email: String)