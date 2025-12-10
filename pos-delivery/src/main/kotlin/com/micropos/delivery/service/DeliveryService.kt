package com.micropos.delivery.service

import com.micropos.model.DeliveryRecord
import com.micropos.model.Order

sealed interface DeliveryService {
    fun getDeliveryInfo(orderId: String): DeliveryRecord?

    fun createDelivery(order: Order): DeliveryRecord

    fun updateDelivery(deliveryRecord: DeliveryRecord): DeliveryRecord?

    fun cancelDelivery(order: Order): DeliveryRecord?

    fun getAllDeliveries(): List<DeliveryRecord>
}
