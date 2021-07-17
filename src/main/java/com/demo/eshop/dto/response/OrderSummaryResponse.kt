package com.demo.eshop.dto.response

class OrderSummaryResponse {
    var id : Long = 0
    var items = emptyList<ItemResponseDto>()
    var totalAmount = 0.0
}
