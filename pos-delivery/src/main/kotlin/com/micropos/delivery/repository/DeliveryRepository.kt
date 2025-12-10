package com.micropos.delivery.repository

import com.micropos.model.DeliveryRecord

interface DeliveryRepository {
    fun saveRecord(delivery: DeliveryRecord): DeliveryRecord

    fun findRecordByOrderId(orderId: String): DeliveryRecord?

    fun findAllRecords(): List<DeliveryRecord>

    fun deleteRecordByOrderId(orderId: String): DeliveryRecord?
}
