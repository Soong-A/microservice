package com.micropos.order.config

import com.micropos.model.Order
import com.micropos.order.service.OrderService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer

@Configuration
class OrderConfig {
    @Autowired
    private lateinit var orderService: OrderService

    private val logger = LoggerFactory.getLogger(OrderConfig::class.java)

    @Bean
    fun onOrderCreated(): Consumer<Order> {
        return Consumer {
            logger.info("Order created: $it")
            orderService.updateOrderStatus(it.id, it.status)
        }
    }
}
