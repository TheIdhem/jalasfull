package ir.faj.jalas.jalas.clients

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

@Component
class SmtpClient {
    fun sendMail(
            username: String,
            password: String,
            host: String,
            port: String,

            destinationEmail: List<String>,
            subject: String,
            textMessage: String,
            mimeMessage: String?,
            from: String
    ) {
        val props = Properties()
        props.put("mail.smtp.auth", "true")
        props.put("mail.smtp.starttls.enable", "false")
        props.put("mail.smtp.host", host)
        props.put("mail.smtp.port", port)
        props.put("mail.smtp.connectiontimeout", "10000")
        props.put("mail.smtp.timeout", "10000")
        props.put("mail.smtp.transport.protocol", "smtp")
        props.put("mail.smtp.localhost", "127.0.0.1")
        props.put("mail.smtp.socketFactory.port", port)
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")


        val session = Session.getInstance(props,
                object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(username, password)
                    }
                })


        try {
            val toAddresses = destinationEmail.map { InternetAddress.parse(it)[0] }.toTypedArray()


            val message = MimeMessage(session)
            val multiPart = MimeMultipart("alternative")

            val textPart = MimeBodyPart()
            textPart.setText(textMessage, "utf-8")

            var htmlPart: MimeBodyPart? = null
            if (mimeMessage != null) {
                htmlPart = MimeBodyPart()
                htmlPart.setContent(mimeMessage, "text/html; charset=utf-8")
            }

            message.setFrom(InternetAddress(from))
            message.setRecipients(Message.RecipientType.TO, toAddresses)
            message.setSubject(subject, "UTF-8")

            multiPart.addBodyPart(textPart)
            if (htmlPart != null)
                multiPart.addBodyPart(htmlPart)
            message.setContent(multiPart)

            Transport.send(message)

            logger.info("Mail sent to {} successfully", destinationEmail)

        } catch (e: MessagingException) {
            logger.error("Error in sending by smtp", e)
        }

    }

    companion object {
        private val logger = LoggerFactory.getLogger(SmtpClient::class.java)
    }

}