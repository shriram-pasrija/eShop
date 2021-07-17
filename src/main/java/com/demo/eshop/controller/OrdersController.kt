package com.demo.eshop.controller

import com.demo.eshop.dto.request.NewOrderRequestDto
import com.demo.eshop.dto.response.OrderSummaryResponse
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
        logger.info("getting order summary for order Id $orderId")
        val orderSummaryResponse = ordersService.getOrderSummary(orderId)
        return ResponseEntity.ok(orderSummaryResponse)
    }

    @GetMapping(value = ["orders"])
    fun getAllOrdersSummary(): ResponseEntity<List<OrderSummaryResponse>> {
        logger.info("getting order summary for all orders")
        val allOrdersSummary = ordersService.getAllOrdersSummary()
        return ResponseEntity.ok(allOrdersSummary)
    }

    @PostMapping(value = ["orders"])
    fun createNewOrder(
        @RequestBody(required = true) request: NewOrderRequestDto
    ): ResponseEntity<OrderSummaryResponse> {
        logger.info("creating a new order")
        val orderSummaryResponse = ordersService.createOrder(request)
        return ResponseEntity(orderSummaryResponse, HttpStatus.CREATED)
    }
}
