package ir.faj.jalas.jalas.utility

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class GmailSender(private val javaMailSender: JavaMailSender) {

    fun sendMail(subject: String, message: String, to: String) {
        val mailMessage = SimpleMailMessage()
        mailMessage.setTo(to)
        mailMessage.setSubject(subject)
        mailMessage.setText(message)

        javaMailSender.send(mailMessage)
    }
}