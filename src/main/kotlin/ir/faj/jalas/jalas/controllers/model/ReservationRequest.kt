package ir.faj.jalas.jalas.controllers.model


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import ir.faj.jalas.jalas.clients.model.RoomReservationRequest
import ir.faj.jalas.jalas.utility.toRoomServiceFormat
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class ReservationRequest(
        var username: String?,
        var optionId: Int,
        var sessionId: Int,
        var startAt: Date?,
        var endAt: Date?
) {
    fun of(): RoomReservationRequest {
        return RoomReservationRequest(username ?: "", (startAt?.toRoomServiceFormat())
                ?: Date().toRoomServiceFormat(), endAt?.toRoomServiceFormat() ?: Date().toRoomServiceFormat())
    }
}