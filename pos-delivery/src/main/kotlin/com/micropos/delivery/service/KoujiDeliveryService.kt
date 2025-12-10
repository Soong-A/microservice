package com.micropos.delivery.service

import com.micropos.delivery.repository.DeliveryRepository
import com.micropos.model.DeliveryRecord
import com.micropos.model.Order
import com.micropos.model.OrderStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Date

@Service
class KoujiDeliveryService : DeliveryService {
    @Autowired
    private lateinit var deliveryRepository: DeliveryRepository

    override fun getDeliveryInfo(orderId: String): DeliveryRecord? {
        return deliveryRepository.findRecordByOrderId(orderId)
    }

    override fun createDelivery(order: Order): DeliveryRecord {
        order.status = OrderStatus.Delivering
        val record = DeliveryRecord(order.id, "Kouji is delivering", Date().time)
        return deliveryRepository.saveRecord(record)
    }

    override fun updateDelivery(deliveryRecord: DeliveryRecord): DeliveryRecord? {
        val order = deliveryRepository.findRecordByOrderId(deliveryRecord.orderId) ?: return null
        deliveryRecord.status = order.status
        return deliveryRepository.saveRecord(deliveryRecord)
    }

    override fun cancelDelivery(order: Order): DeliveryRecord? {
        order.status = OrderStatus.Canceled
        return deliveryRepository.deleteRecordByOrderId(order.id)
    }

    override fun getAllDeliveries(): List<DeliveryRecord> {
        return deliveryRepository.findAllRecords()
    }
}
