package com.demo.eshop.service.impl

import com.demo.eshop.da.entity.ItemEntity
import com.demo.eshop.da.entity.OfferEntity
import com.demo.eshop.da.entity.OrderEntity
import com.demo.eshop.da.entity.OrderItemEntity
import com.demo.eshop.da.repo.ItemRepo
import com.demo.eshop.da.repo.OfferRepo
import com.demo.eshop.da.repo.OrdersRepo
import com.demo.eshop.dto.request.ItemRequestDto
import com.demo.eshop.dto.request.NewOrderRequestDto
import com.demo.eshop.dto.response.AvailableItemResponseDto
import com.demo.eshop.dto.response.OrderSummaryResponse
import com.demo.eshop.exceptions.OrderNotFoundException
import com.demo.eshop.service.OrdersService
import com.demo.eshop.util.anyObjectHelper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyLong
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class OrdersServiceImplTest {

    @Mock
    lateinit var itemRepo: ItemRepo

    @Mock
    lateinit var ordersRepo: OrdersRepo

    @Mock
    lateinit var offerRepo: OfferRepo

    private lateinit var ordersService: OrdersService

    private val appleItem = ItemEntity().apply {
        id = 1
        name = "Apple"
        price = 60.0
    }

    private val orangeItem = ItemEntity().apply {
        id = 2
        name = "Orange"
        price = 25.0
    }

    private val offerOnApples = OfferEntity().apply {
        id = 1
        item = appleItem
        name = "Buy 1 Get 1"
        isActive = true
    }

    private val offerOnOranges = OfferEntity().apply {
        id = 2
        item = orangeItem
        name = "Get 3 for price of 2"
        isActive = true
    }

    @BeforeEach
    fun setUp() {
        ordersService = OrdersServiceImpl()
        (ordersService as OrdersServiceImpl).ordersRepo = ordersRepo
        (ordersService as OrdersServiceImpl).itemRepo = itemRepo
        (ordersService as OrdersServiceImpl).offerRepo = offerRepo
    }

    @Test
    fun getOrderSummarySuccess() {
        val expectedResponse = OrderSummaryResponse().apply {
            id = 1
            items = mutableListOf(
                AvailableItemResponseDto(1, "Apple", 2, 60.0, 120.0),
                AvailableItemResponseDto(2, "Orange", 4, 25.0, 100.0)
            )
            totalAmount = 220.0
        }

        val mockOrderEntity = OrderEntity().apply {
            id = 1
            amount = 220.0
            items = mutableListOf(OrderItemEntity().apply {
                id = 1
                item = appleItem
                quantity = 2
            }, OrderItemEntity().apply {
                id = 2
                item = orangeItem
                quantity = 4
            })
        }

        `when`(ordersRepo.findById(anyLong())).thenReturn(Optional.of(mockOrderEntity))

        //then
        val response = ordersService.getOrderSummary(1)
        assertNotNull(response)
        assert(response.id == expectedResponse.id)
        assert(response.totalAmount.equals(expectedResponse.totalAmount))
        assertNotNull(response.items)
    }

    @Test
    fun getOrderSummaryFailureForOrderThatDoesNotExists() {
        `when`(ordersRepo.findById(anyLong())).thenReturn(Optional.empty())
        //then
        Assertions.assertThrows(OrderNotFoundException::class.java) {
            ordersService.getOrderSummary(1)
        }
    }

    @Test
    fun createOrderSuccessWithoutDiscount() {
        val expectedResponse = OrderSummaryResponse().apply {
            id = 1
            items = mutableListOf(
                AvailableItemResponseDto(1, "Apple", 2, 60.0, 120.0),
                AvailableItemResponseDto(2, "Orange", 4, 25.0, 100.0)
            )
            totalAmount = 220.0
        }
        val mockOrderEntity = OrderEntity().apply {
            id = 1
            amount = 220.0
            items = mutableListOf(OrderItemEntity().apply {
                id = 1
                item = appleItem
                quantity = 2
            }, OrderItemEntity().apply {
                id = 2
                item = orangeItem
                quantity = 4
            })
        }
        val request = NewOrderRequestDto().apply {
            items = listOf(ItemRequestDto().apply {
                id = 1
                quantity = 2
            }, ItemRequestDto().apply {
                id = 2
                quantity = 4
            })
        }

        `when`(ordersRepo.save(anyObjectHelper())).thenReturn(mockOrderEntity)
        `when`(itemRepo.findById(ArgumentMatchers.eq(1L))).thenReturn(Optional.of(appleItem))
        `when`(itemRepo.findById(ArgumentMatchers.eq(2L))).thenReturn(Optional.of(orangeItem))

        val response = ordersService.createOrder(request)
        assertNotNull(response)
        assert(response.id == expectedResponse.id)
        assert(response.totalAmount.equals(expectedResponse.totalAmount))
        assertNotNull(response.items)
    }

    @Test
    fun createOrderSuccessWithDiscount() {
        val expectedResponse = OrderSummaryResponse().apply {
            id = 1
            items = mutableListOf(
                AvailableItemResponseDto(1, "Apple", 4, 60.0, 120.0),
                AvailableItemResponseDto(2, "Orange", 3, 25.0, 50.0)
            )
            totalAmount = 170.0
        }
        val mockOrderEntity = OrderEntity().apply {
            id = 1
            amount = 170.0
            items = mutableListOf(OrderItemEntity().apply {
                id = 1
                item = appleItem
                quantity = 4
                amount = 120.0
            }, OrderItemEntity().apply {
                id = 2
                item = orangeItem
                quantity = 3
                amount = 50.0
            })
        }
        val request = NewOrderRequestDto().apply {
            items = listOf(ItemRequestDto().apply {
                id = 1
                quantity = 4
            }, ItemRequestDto().apply {
                id = 2
                quantity = 3
            })
        }

        `when`(ordersRepo.save(anyObjectHelper())).thenReturn(mockOrderEntity)
        `when`(itemRepo.findById(ArgumentMatchers.eq(1L))).thenReturn(Optional.of(appleItem))
        `when`(itemRepo.findById(ArgumentMatchers.eq(2L))).thenReturn(Optional.of(orangeItem))
        `when`(offerRepo.findByItemId(ArgumentMatchers.eq(1L))).thenReturn(Optional.of(offerOnApples))
        `when`(offerRepo.findByItemId(ArgumentMatchers.eq(2L))).thenReturn(Optional.of(offerOnOranges))

        val response = ordersService.createOrder(request)
        assertNotNull(response)
        assert(response.id == expectedResponse.id)
        assert(response.totalAmount.equals(expectedResponse.totalAmount))
        assertNotNull(response.items)

        val appleResponse = response.items.map { it as AvailableItemResponseDto }.find { it.id == 1L }
        assertEquals((expectedResponse.items[0] as AvailableItemResponseDto).amount, appleResponse?.amount)
    }
}
