package ir.faj.jalas.jalas.controllers

import ir.faj.jalas.jalas.controllers.model.ReportResponse
import ir.faj.jalas.jalas.service.session.SessionService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1.0/report")
class ReportController(val sessionService: SessionService) {

    @GetMapping("/all")
    fun getAvrageTimeOfSessions(): ReportResponse {
        return sessionService.getAvrageTimeSession()
    }


}