package com.demo.eshop.controller

import com.demo.eshop.dto.NewOrderRequestDto
import com.demo.eshop.dto.OrderSummaryResponse
import com.demo.eshop.service.OrdersService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class OrdersController {

    private val logger = LoggerFactory.getLogger(OrdersController::class.java)

    @Autowired
    lateinit var ordersService: OrdersService

    @GetMapping(value = ["orders/{orderId}"])
    fun getOrderSummary(@PathVariable("orderId") orderId: Long): ResponseEntity<OrderSummaryResponse> {
        logger.info("getting order summary")
        val orderSummaryResponse = ordersService.getOrderSummary(orderId)
        return ResponseEntity.ok(orderSummaryResponse)
    }

    @PostMapping(value = ["orders"])
    fun createNewOrder(
        @RequestBody(required = true) request: NewOrderRequestDto
    ): ResponseEntity<OrderSummaryResponse> {
        logger.info("getting order summary")
        val orderSummaryResponse = ordersService.createOrder(request)
        return ResponseEntity(orderSummaryResponse, HttpStatus.CREATED)
    }
}
