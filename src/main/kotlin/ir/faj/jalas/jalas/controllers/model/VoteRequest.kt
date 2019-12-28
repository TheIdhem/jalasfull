package ir.faj.jalas.jalas.controllers.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import ir.faj.jalas.jalas.enums.VoteType

@JsonIgnoreProperties(ignoreUnknown = true)
data class VoteRequest(val agreeOptionIds: List<Int>,
                       val disAgreeOptionIds: List<Int>?)

data class SingleVoteRequest(val optionId: Int,
                             val status: VoteType)

