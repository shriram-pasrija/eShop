package com.demo.eshop.exceptions.response

import java.time.LocalDateTime
import java.time.ZonedDateTime

data class ExceptionResponse(
    var message: String?,
    var timeInMillis: Long
)