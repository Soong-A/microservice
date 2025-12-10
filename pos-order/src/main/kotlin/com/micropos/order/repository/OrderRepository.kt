package com.micropos.order.repository

import com.micropos.model.Order

sealed interface OrderRepository {
    fun getNextOrderId(): String
    fun getAllOrders(): Collection<Order>
    fun getOrderById(id: String): Order?
    fun saveOrder(order: Order)
    fun deleteOrder(id: String): Order?
    fun updateStatus(id: String, status: String)
}
