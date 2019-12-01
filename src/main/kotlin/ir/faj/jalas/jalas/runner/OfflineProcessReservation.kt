package ir.faj.jalas.jalas.runner

import ir.faj.jalas.jalas.controllers.model.ReservationRequest
import ir.faj.jalas.jalas.entities.repository.EventLogRepository
import ir.faj.jalas.jalas.enums.EventLogType
import ir.faj.jalas.jalas.service.session.SessionService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class OfflineProcessReservation(val events: EventLogRepository,
                                val sessionService: SessionService) {

    @Scheduled(fixedDelay = 3 * 60 * 1000)
    fun offlineProcess() {
        events.findAllByChecked().forEach {
            try {
                val reservationRequest = ReservationRequest(it.session.owner.username, it.session.startAt, it.session.endAt)
                sessionService.reservSession(it.session, reservationRequest)
                it.eventType = EventLogType.passed
            } catch (ex: Exception) {
                //hiss dokhtarha faryad nemizannand
            }
            it.checked = true
            events.save(it)
        }
    }
}