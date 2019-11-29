package ir.faj.jalas.jalas.configurations

import ir.faj.jalas.jalas.utility.EmailSender
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by r on 4/23/18.
 */
@Configuration
open class SmtpConfiguration {

    @Value("\${jalas.email.host}")
    lateinit var host: String

    @Value("\${jalas.email.tls}")
    lateinit var tls: String

    @Value("\${jalas.email.username}")
    lateinit var username: String

    @Value("\${jalas.email.password}")
    lateinit var password: String


    @Bean
    open fun emailSender(): EmailSender {
        return EmailSender(host, tls.toBoolean(), username, password)
    }
}