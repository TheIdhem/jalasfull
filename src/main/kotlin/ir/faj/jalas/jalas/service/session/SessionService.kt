package ir.faj.jalas.jalas.service.session

import ir.faj.jalas.jalas.clients.model.AvailableRoomResponse
import java.util.*

interface SessionService {
    fun getAvailableRoom(startAt: Date, endAt:Date): AvailableRoomResponse
}