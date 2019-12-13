package ir.faj.jalas.jalas.controllers.model

data class VoteRequest(val agreeOptionIds: List<Int>,
                       val disAgreeOptionIds: List<Int>,
                       val username: String)