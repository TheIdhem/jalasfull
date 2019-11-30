package ir.faj.jalas.jalas.utility

import ir.faj.jalas.jalas.clients.SmtpClient
import org.springframework.stereotype.Component


@Component
class GmailSender(private val smtpClient: SmtpClient) {

    fun send(subject: String, htmlText: String, destination: List<String>) {
        smtpClient.sendMail(
                username = "info.jalas.prod@gmail.com",
                password = "JAVad1303",
                host = "smtp.gmail.com",
                port = "465",
                destinationEmail = destination,
                subject = subject,
                textMessage = "",
                mimeMessage = htmlText,
                from = "info.jalas.prod@gmail.com"
        )
    }

    fun sendToUser(subject: String, htmlText: String, destination: List<String>) {
        smtpClient.sendMail(
                username = "info.jalas.prod@gmail.com",
                password = "JAVad1303",
                host = "smtp.gmail.com",
                port = "465",
                destinationEmail = destination,
                subject = subject,
                textMessage = "",
                mimeMessage = htmlText,
                from = "info.jalas.prod@gmail.com"
        )
    }
}