package ir.faj.jalas.jalas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableJpaRepositories
@EnableTransactionManagement
open class JalasApplication

fun main(args: Array<String>) {
    runApplication<JalasApplication>(*args)
}
