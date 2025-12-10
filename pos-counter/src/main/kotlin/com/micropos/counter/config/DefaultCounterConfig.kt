package com.micropos.counter.config

import org.springframework.context.annotation.Configuration

@Configuration
class DefaultCounterConfig {
    val discount: Double get() = 0.1
    val tax: Double get() = 0.3
}
