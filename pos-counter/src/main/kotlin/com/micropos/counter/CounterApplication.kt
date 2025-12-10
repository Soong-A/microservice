package com.micropos.counter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@SpringBootApplication
@EnableEurekaClient
class CounterApplication

fun main(args: Array<String>) {
    runApplication<CounterApplication>(*args)
}
