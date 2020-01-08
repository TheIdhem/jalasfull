package ir.faj.jalas.jalas.controllers.model

import ir.faj.jalas.jalas.enums.SessionStatus

data class SessionStatusRequest (val sessionId:Int,val status: SessionStatus)
