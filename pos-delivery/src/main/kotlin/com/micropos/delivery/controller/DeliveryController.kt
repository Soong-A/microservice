package com.micropos.delivery.controller

import com.micropos.api.DeliveryApi
import com.micropos.delivery.service.DeliveryService
import com.micropos.dto.DeliveryRecordDto
import com.micropos.mapper.DeliveryMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class DeliveryController : DeliveryApi {
    @Autowired
    lateinit var deliveryService: DeliveryService

    @Autowired
    lateinit var deliveryMapper: DeliveryMapper

    override fun getDeliveryInfo(): ResponseEntity<MutableList<DeliveryRecordDto>> {
        return ResponseEntity.ok(
            deliveryMapper.toDto(deliveryService.getAllDeliveries()).toMutableList()
        )
    }

    override fun getOrderDeliveryInfo(orderId: String?): ResponseEntity<DeliveryRecordDto> {
        return orderId?.let {
            deliveryService.getDeliveryInfo(orderId)?.let {
                ResponseEntity.ok(deliveryMapper.toDto(it))
            }
        } ?: ResponseEntity.notFound().build()
    }
}
