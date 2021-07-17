package com.demo.eshop.controller

import com.demo.eshop.dto.AvailableItemResponseDto
import com.demo.eshop.dto.ItemRequestDto
import com.demo.eshop.dto.NewOrderRequestDto
import com.demo.eshop.dto.OrderSummaryResponse
import com.demo.eshop.exceptions.handler.GlobalExceptionHandler
import com.demo.eshop.service.OrdersService
import com.demo.eshop.util.anyObjectHelper
import com.demo.eshop.util.toJsonString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders


@SpringBootTest
@ExtendWith(MockitoExtension::class)
internal class OrdersControllerTest {

    private lateinit var mockMvc: MockMvc

    @Mock
    lateinit var ordersService: OrdersService

    @InjectMocks
    lateinit var ordersController: OrdersController

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ordersController).setControllerAdvice(GlobalExceptionHandler())
            .build()
    }

    @Test
    fun getOrderSummary() {
        val mockResponse = OrderSummaryResponse().apply {
            id = 1
            items = mutableListOf(
                AvailableItemResponseDto(1, "Apple", 2, 60.0),
                AvailableItemResponseDto(2, "Orange", 4, 25.0)
            )
            totalAmount = 220.0
        }
        `when`(ordersService.getOrderSummary(anyLong())).thenReturn(mockResponse)
        mockMvc.perform(get("/orders/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk).andExpect(content().json(mockResponse.toJsonString()))
    }

    @Test
    fun createNewOrder() {
        val mockResponse = OrderSummaryResponse().apply {
            id = 1
            items = mutableListOf(
                AvailableItemResponseDto(1, "Apple", 2, 60.0),
                AvailableItemResponseDto(2, "Orange", 4, 25.0)
            )
            totalAmount = 220.0
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

        `when`(ordersService.createOrder(anyObjectHelper())).thenReturn(mockResponse)
        mockMvc.perform(post("/orders/").content(request.toJsonString()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)
            .andExpect(content().json(mockResponse.toJsonString()))
    }
}