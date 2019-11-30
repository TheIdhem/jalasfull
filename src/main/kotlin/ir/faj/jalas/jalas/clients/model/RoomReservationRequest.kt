package ir.faj.jalas.jalas.clients.model

data class RoomReservationRequest(
        var username: String,
        var start: String,
        var end: String
)