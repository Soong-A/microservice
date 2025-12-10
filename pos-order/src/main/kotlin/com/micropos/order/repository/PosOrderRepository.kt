package com.micropos.order.repository

import com.micropos.model.Order
import com.micropos.model.OrderStatus
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

@Repository
class PosOrderRepository : OrderRepository {
    private val orders = ConcurrentHashMap<String, Order>()

    override fun getNextOrderId(): String {
        while (true) {
            val orderId = Random.nextLong().toString()
            if (!orders.containsKey(orderId)) {
                return orderId
            }
        }
    }

    override fun getAllOrders(): Collection<Order> {
        return orders.values
    }

    override fun getOrderById(id: String): Order? {
        return orders[id]
    }

    override fun saveOrder(order: Order) {
        orders[order.id] = order
    }

    override fun deleteOrder(id: String): Order? {
        return orders.remove(id)
    }

    override fun updateStatus(id: String, status: String) {
        orders[id]?.status = OrderStatus.valueOf(status)
    }
}
