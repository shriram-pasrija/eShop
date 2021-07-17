package com.demo.eshop.dto

interface ItemResponseDto

data class AvailableItemResponseDto(
    var id: Long,
    var name: String,
    var quantity: Int,
    var price: Double
) : ItemResponseDto

class NotAvailableItemResponseDto(var id: Long, var message: String = "Item with id $id not available") : ItemResponseDto
