package com.demo.eshop.service.impl

import com.demo.eshop.da.entity.OfferEntity
import com.demo.eshop.da.entity.OrderEntity
import com.demo.eshop.da.entity.OrderItemEntity
import com.demo.eshop.da.entity.toOrderSummaryResponse
import com.demo.eshop.da.repo.ItemRepo
import com.demo.eshop.da.repo.OfferRepo
import com.demo.eshop.da.repo.OrdersRepo
import com.demo.eshop.dto.request.NewOrderRequestDto
import com.demo.eshop.dto.response.ItemResponseDto
import com.demo.eshop.dto.response.NotAvailableItemResponseDto
import com.demo.eshop.dto.response.OrderSummaryResponse
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

    @Autowired
    lateinit var offerRepo: OfferRepo

    override fun getOrderSummary(orderId: Long): OrderSummaryResponse {
        return ordersRepo.findById(orderId).map { orderEntity ->
            orderEntity.toOrderSummaryResponse()
        }.orElseThrow { OrderNotFoundException() }
    }

    override fun createOrder(orderRequest: NewOrderRequestDto): OrderSummaryResponse {
        var billAmount = 0.0
        val orderEntity = OrderEntity()
        val itemsNotPresent = mutableListOf<ItemResponseDto>()
        orderRequest.items?.filter { it.quantity > 0 }?.forEach { dto ->
            val optionalItemEntity = itemRepo.findById(dto.id)
            //check if items are not available
            if (optionalItemEntity.isEmpty) {
                itemsNotPresent.add(NotAvailableItemResponseDto(dto.id))
            } else {
                val itemEntity = optionalItemEntity.get()
                val offer = offerRepo.findByItemId(itemEntity.id).orElse(null)
                val orderItemEntity = OrderItemEntity()
                orderItemEntity.item = itemEntity
                orderItemEntity.quantity = dto.quantity
                val priceOfItems = applyOfferAndCalculateAmount(orderItemEntity, dto.quantity, offer)
                orderItemEntity.amount = priceOfItems
                billAmount += priceOfItems
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

    private fun applyOfferAndCalculateAmount(orderItem: OrderItemEntity, quantity: Int, offer: OfferEntity?): Double {

        val amountWithoutOffer = orderItem.item.price * quantity
        if (offer == null || !offer.isActive) {
            return amountWithoutOffer
        }
        val n: Int
        var amount = 0.0
        when (offer.id) {
            1 -> {
                n = 2
                val q = quantity / n
                val r = quantity % n
                amount = (q + r) * orderItem.item.price
            }

            2 -> {
                n = 3
                val q = quantity / n
                val r = quantity % n
                amount = (2 * q + r) * orderItem.item.price
            }
        }
        if (amountWithoutOffer > amount) {
            orderItem.offer = offer
        }
        return amount
    }
}
