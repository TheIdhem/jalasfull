package ir.faj.jalas.jalas.controllers.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import ir.faj.jalas.jalas.dto.rdbms.SessionOptionShallowDto

@JsonIgnoreProperties(ignoreUnknown = true)
data class SessionRequest(val sessionId: Int = 0, val title: String, val users: List<String>, val options: List<SessionOptionShallowDto>)