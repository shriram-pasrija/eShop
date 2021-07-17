package com.demo.eshop.service

import com.demo.eshop.dto.NewOrderRequestDto
import com.demo.eshop.dto.OrderSummaryResponse
import org.springframework.http.ResponseEntity

interface OrdersService {
    fun createOrder(orderRequest: NewOrderRequestDto): OrderSummaryResponse
    fun getOrderSummary(orderId: Long): OrderSummaryResponse
}
