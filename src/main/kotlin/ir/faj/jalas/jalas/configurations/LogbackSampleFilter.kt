package ir.faj.jalas.jalas.configurations

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply

class LogbackSampleFilter: Filter<ILoggingEvent>() {
    override fun decide(event: ILoggingEvent): FilterReply {
        return if (Math.random() < 0.1 || event.level.isGreaterOrEqual(Level.WARN))
            FilterReply.ACCEPT
        else
            FilterReply.DENY
    }
}
