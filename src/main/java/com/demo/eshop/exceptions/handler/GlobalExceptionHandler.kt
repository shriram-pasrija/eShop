package com.demo.eshop.exceptions.handler

import com.demo.eshop.exceptions.OrderNotFoundException
import com.demo.eshop.exceptions.response.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.time.Instant

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException::class)
    fun handleExceptions(exception: OrderNotFoundException): ResponseEntity<Any> {
        val response = ExceptionResponse(exception.message, Instant.now().toEpochMilli())
        return ResponseEntity(response, HttpStatus.NOT_FOUND)
    }
}

