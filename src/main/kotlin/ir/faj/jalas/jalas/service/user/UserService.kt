package ir.faj.jalas.jalas.service.user

import ir.faj.jalas.jalas.entities.Session

interface UserService {
    fun getUserSession(username:String): List<Session>
}