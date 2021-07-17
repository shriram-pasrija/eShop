package com.demo.eshop.service.impl

import com.demo.eshop.da.entity.OrderEntity
import com.demo.eshop.da.entity.OrderItemEntity
import com.demo.eshop.da.entity.toOrderSummaryResponse
import com.demo.eshop.da.repo.ItemRepo
import com.demo.eshop.da.repo.OrdersRepo
import com.demo.eshop.dto.ItemResponseDto
import com.demo.eshop.dto.NewOrderRequestDto
import com.demo.eshop.dto.NotAvailableItemResponseDto
import com.demo.eshop.dto.OrderSummaryResponse
import com.demo.eshop.exceptions.OrderNotFoundException
import com.demo.eshop.service.OrdersService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrdersServiceImpl : OrdersService {

    @Autowired
    lateinit var ordersRepo: OrdersRepo

    @Autowired
    lateinit var itemRepo: ItemRepo

    override fun getOrderSummary(orderId: Long): OrderSummaryResponse {
        return ordersRepo.findById(orderId).map { orderEntity ->
            orderEntity.toOrderSummaryResponse()
        }.orElseThrow { OrderNotFoundException() }
    }

    override fun createOrder(orderRequest: NewOrderRequestDto): OrderSummaryResponse {
        var billAmount = 0.0
        val orderEntity = OrderEntity()
        val itemsNotPresent = mutableListOf<ItemResponseDto>()
        orderRequest.items?.forEach { dto ->
            val optionalItemEntity = itemRepo.findById(dto.id)
            if (optionalItemEntity.isEmpty) {
                itemsNotPresent.add(NotAvailableItemResponseDto(dto.id))
            } else {
                val itemEntity = optionalItemEntity.get()
                billAmount += itemEntity.price * dto.quantity
                val orderItemEntity = OrderItemEntity()
                orderItemEntity.item = itemEntity
                orderItemEntity.quantity = dto.quantity
                orderEntity.items.add(orderItemEntity)
            }
        }

        orderEntity.amount = billAmount
        val response: OrderSummaryResponse = if (orderEntity.items.isNotEmpty()) {
            val savedOrder = ordersRepo.save(orderEntity)
            savedOrder.toOrderSummaryResponse()
        } else {
            orderEntity.toOrderSummaryResponse()
        }
        val allItems = response.items.toMutableList()
        allItems.addAll(itemsNotPresent)
        response.items = allItems
        return response
    }
}
