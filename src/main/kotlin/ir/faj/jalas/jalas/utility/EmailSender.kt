package ir.faj.jalas.jalas.utility

import java.util.*
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart


/**
 * Created by r on 4/23/18.
 */
class EmailSender(host: String, tls: Boolean, private val username: String, private val password: String) {

    private var session: Session

    init {
        val props = Properties()
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "$tls"
        props["mail.smtp.host"] = host
        props["mail.smtp.port"] = "587"

        session = Session.getInstance(props, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })
    }

    fun send(from: String, to: String, subject: String, plainText: String, htmlText: String) {
        val message = MimeMessage(session)
        message.setFrom(InternetAddress(from))
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
        message.subject = subject

        val textPart = MimeBodyPart()
        textPart.setContent(plainText, "text/plain; charset=utf-8")

        val htmlPart = MimeBodyPart()
        htmlPart.setContent(htmlText, "text/html; charset=utf-8")

        val mp = MimeMultipart("alternative")
        mp.addBodyPart(textPart)
        mp.addBodyPart(htmlPart)

        message.setContent(mp)
        Transport.send(message)

    }
}