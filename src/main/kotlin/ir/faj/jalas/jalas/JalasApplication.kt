package ir.faj.jalas.jalas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
open class JalasApplication

fun main(args: Array<String>) {
    runApplication<JalasApplication>(*args)
}
