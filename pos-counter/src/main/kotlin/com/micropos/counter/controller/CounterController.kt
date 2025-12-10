package com.micropos.counter.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.micropos.api.CheckoutApi
import com.micropos.counter.config.DefaultCounterConfig
import com.micropos.dto.CheckoutItemDto
import com.micropos.dto.InlineResponse200Dto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/api")
class CounterController : CheckoutApi {
    @Autowired
    lateinit var counterConfig: DefaultCounterConfig

    override fun calculateOrderTotal(checkoutItemDto: MutableList<CheckoutItemDto>?): ResponseEntity<InlineResponse200Dto> {
        val total = checkoutItemDto?.let { it.sumOf { item -> item.price * item.quantity.toDouble() } }
        return total?.let {
            val response = InlineResponse200Dto()
            response.total = it * (1 - counterConfig.discount) * (1 + counterConfig.tax)
            ResponseEntity.ok(response)
        } ?: ResponseEntity.notFound().build()
    }
}
