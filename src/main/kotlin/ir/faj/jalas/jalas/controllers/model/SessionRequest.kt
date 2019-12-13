package ir.faj.jalas.jalas.controllers.model

import ir.faj.jalas.jalas.dto.rdbms.SessionOptionShallowDto

data class SessionRequest(val title: String, val users: List<String>,val options:List<SessionOptionShallowDto>,val username:String)