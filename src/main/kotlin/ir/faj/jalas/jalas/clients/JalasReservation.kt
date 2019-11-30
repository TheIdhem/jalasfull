package ir.faj.jalas.jalas.clients

import ir.faj.jalas.jalas.clients.model.AvailableRoomResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestParam


@FeignClient(name = "jalas-reservation", url = "http://213.233.176.40")
interface JalasReservation {

    @GetMapping("/available_rooms")
    fun getAvailableRoom(@RequestParam start: String, @RequestParam end: String): AvailableRoomResponse

}