package com.micropos.order.service

import com.micropos.dto.ItemDto
import com.micropos.model.Order
import com.micropos.model.OrderStatus

sealed interface OrderService {
    fun getAllOrders(): List<Order>
    fun getOrderById(id: String): Order?
    fun createOrder(items: List<ItemDto>): Order
    fun updateOrder(id: String, items: List<ItemDto>): Order?
    fun deleteOrder(id: String): Order?
    fun updateOrderStatus(id: String, status: OrderStatus): Order?
}
