package com.demo.eshop.controller

import com.demo.eshop.dto.request.NewOrderRequestDto
import com.demo.eshop.dto.response.OrderSummaryResponse
import com.demo.eshop.service.OrdersService
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class OrdersController {

    private val logger = LoggerFactory.getLogger(OrdersController::class.java)

    @Autowired
    lateinit var ordersService: OrdersService

    @GetMapping(value = ["orders/{orderId}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(
        value = "orders",
        notes = "Endpoint to get order summary of a given order",
        response = OrderSummaryResponse::class,
    )
    fun getOrderSummary(@PathVariable("orderId") orderId: Long): ResponseEntity<OrderSummaryResponse> {
        logger.info("getting order summary for order Id $orderId")
        val orderSummaryResponse = ordersService.getOrderSummary(orderId)
        return ResponseEntity.ok(orderSummaryResponse)
    }

    @GetMapping(value = ["orders"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(
        value = "orders",
        notes = "Endpoint to get order summary for all orders",
        response = OrderSummaryResponse::class,
        responseContainer = "List",
    )
    fun getAllOrdersSummary(): ResponseEntity<List<OrderSummaryResponse>> {
        logger.info("getting order summary for all orders")
        val allOrdersSummary = ordersService.getAllOrdersSummary()
        return ResponseEntity.ok(allOrdersSummary)
    }

    @PostMapping(
        value = ["orders"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ApiOperation(
        value = "orders",
        notes = "Endpoint to create a new order",
        response = OrderSummaryResponse::class,
        responseContainer = "List"
    )
    fun createNewOrder(
        @RequestBody(required = true) request: NewOrderRequestDto
    ): ResponseEntity<OrderSummaryResponse> {
        logger.info("creating a new order")
        val orderSummaryResponse = ordersService.createOrder(request)
        return ResponseEntity(orderSummaryResponse, HttpStatus.CREATED)
    }
}
