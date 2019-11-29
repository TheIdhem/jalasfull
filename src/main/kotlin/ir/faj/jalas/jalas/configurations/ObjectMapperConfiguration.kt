package ir.faj.jalas.jalas.configurations

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

/**
 * Created by r on 5/19/18.
 */
@Configuration
open class ObjectMapperConfiguration(val mapper: ObjectMapper) {

    @PostConstruct
    fun construct() {
        mapper.registerModule(KotlinModule())
    }
}