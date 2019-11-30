package ir.faj.jalas.jalas.clients

import ir.faj.jalas.jalas.clients.model.AvailableRoomResponse
import ir.faj.jalas.jalas.clients.model.RoomReservationRequest
import ir.faj.jalas.jalas.clients.model.RoomReservationResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*


@FeignClient(name = "jalas-reservation", url = "http://213.233.176.40")
interface JalasReservation {

    @GetMapping("/available_rooms")
    fun getAvailableRoom(@RequestParam start: String, @RequestParam end: String): AvailableRoomResponse

    @PostMapping("rooms/{roomId}/reserve")
    fun reserveRoom(@PathVariable roomId: Int, @RequestBody request: RoomReservationRequest): RoomReservationResponse

}