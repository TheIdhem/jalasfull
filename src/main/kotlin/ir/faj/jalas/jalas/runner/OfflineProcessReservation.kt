package ir.faj.jalas.jalas.runner

import ir.faj.jalas.jalas.controllers.model.ReservationRequest
import ir.faj.jalas.jalas.entities.Session
import ir.faj.jalas.jalas.entities.User
import ir.faj.jalas.jalas.entities.repository.EventLogRepository
import ir.faj.jalas.jalas.entities.repository.NotificationRepository
import ir.faj.jalas.jalas.enums.EventLogType
import ir.faj.jalas.jalas.enums.NotificationType
import ir.faj.jalas.jalas.service.session.SessionService
import ir.faj.jalas.jalas.utility.GmailSender
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class OfflineProcessReservation(val events: EventLogRepository,
                                val sessionService: SessionService,
                                val gmailSender: GmailSender,
                                val notifications: NotificationRepository) {

    @Scheduled(fixedDelay = 3 * 60 * 1000)
    fun offlineProcess() {
        events.findAllByCheckedAndLogStatus(EventLogType.shouldRequestAgain).forEach { event ->
            try {
                val reservationRequest = ReservationRequest(event.session.owner.username, 0, 0, event.session.startAt, event.session.endAt)
                sessionService.reservSession(event.session, reservationRequest, true)
                event.eventType = EventLogType.passed
            } catch (ex: Exception) {
                println(ex.cause)
                //hiss dokhtarha faryad nemizannand

            }
            event.checked = true
            events.save(event)
        }
    }

    @Scheduled(fixedDelay = 10 * 60 * 1000)
    fun emailForUnavailableRequest() {
        events.findAllByCheckedAndLogStatus(EventLogType.roomNotAvailable).forEach { event ->
            try {
                notifyUnSuccessReservation(event.session.owner, event.session)
            } catch (ex: Exception) {
                //hiss dokhtarha faryad nemizannand

            }
            event.checked = true
            events.save(event)
        }
    }

    private fun notifyUnSuccessReservation(user: User, session: Session) {
        if (notifications.findByUserIdAndType(user.id, NotificationType.reservationSession) == null)
            gmailSender.sendMail(
                    subject = "Meeting Reservation failed",
                    message = """
                            |Dear ${session.owner.name},
                            |
                            |Your meeting '${session.title}' at time [${session.startAt}, ${session.endAt}] has been failed because room not available.
                            |
                            |Best Regards,
                            |HamoonHamishegi Team
                        """.trimMargin(),
                    to = user.email
            )
    }
}