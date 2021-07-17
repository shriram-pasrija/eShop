package com.demo.eshop.service

import com.demo.eshop.dto.request.NewOrderRequestDto
import com.demo.eshop.dto.response.OrderSummaryResponse

interface OrdersService {
    fun createOrder(orderRequest: NewOrderRequestDto): OrderSummaryResponse
    fun getOrderSummary(orderId: Long): OrderSummaryResponse
}
