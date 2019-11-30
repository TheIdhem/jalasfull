package ir.faj.jalas.jalas.service.session

import ir.faj.jalas.jalas.clients.JalasReservation
import ir.faj.jalas.jalas.clients.model.AvailableRoomResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Locale


@Service
class SessionServiceImpl(val jalasReservation: JalasReservation) : SessionService {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun getAvailableRoom(startAt: Date, endAt: Date): AvailableRoomResponse {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val start = inputFormat.format(startAt)
            val end = inputFormat.format(endAt)

            return jalasReservation.getAvailableRoom(start, end)
        } catch (ex: Exception) {
            logger.info("error in get rooms from jalas service room : ex:{${ex.cause}}")
            return AvailableRoomResponse()
        }

    }

}


