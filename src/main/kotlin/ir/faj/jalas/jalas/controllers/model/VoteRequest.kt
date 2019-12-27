package ir.faj.jalas.jalas.controllers.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class VoteRequest(val agreeOptionIds: List<Int>,
                       val disAgreeOptionIds: List<Int>?)