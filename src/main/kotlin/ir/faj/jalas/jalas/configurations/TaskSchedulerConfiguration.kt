package ir.faj.jalas.jalas.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

@Configuration
open class TaskSchedulerConfiguration {

    @Bean
    open fun taskScheduler(): ThreadPoolTaskScheduler =
            ThreadPoolTaskScheduler().apply { poolSize = 10 }
}
