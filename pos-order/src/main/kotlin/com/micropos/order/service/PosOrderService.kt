package com.micropos.order.service

import com.micropos.dto.ItemDto
import com.micropos.model.Item
import com.micropos.model.Order
import com.micropos.model.OrderStatus
import com.micropos.order.repository.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.stereotype.Service

@Service
class PosOrderService : OrderService {
    @Autowired
    lateinit var orderRepository: OrderRepository

    @Autowired
    lateinit var streamBridge: StreamBridge

    override fun getAllOrders(): List<Order> {
        return orderRepository.getAllOrders().toList()
    }

    override fun getOrderById(id: String): Order? {
        return orderRepository.getOrderById(id)
    }

    override fun createOrder(items: List<ItemDto>): Order {
        val order = Order(
            orderRepository.getNextOrderId(),
            items.map { Item(it.productId, it.quantity.intValueExact()) },
            OrderStatus.ToSend,
        )
        orderRepository.saveOrder(order)

        streamBridge.send("ordering", order)
        return order
    }

    override fun updateOrder(id: String, items: List<ItemDto>): Order? {
        val order = orderRepository.getOrderById(id) ?: return null
        val newOrder = Order(
            order.id,
            items.map { Item(it.productId, it.quantity.intValueExact()) },
            order.status,
        )
        orderRepository.saveOrder(newOrder)
        return newOrder
    }

    override fun deleteOrder(id: String): Order? {
        return orderRepository.deleteOrder(id)
    }

    override fun updateOrderStatus(id: String, status: OrderStatus): Order? {
        val order = orderRepository.getOrderById(id) ?: return null
        val newOrder = Order(
            order.id,
            order.items,
            status,
        )
        orderRepository.saveOrder(newOrder)
        return newOrder
    }
}
