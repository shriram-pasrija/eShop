package com.demo.eshop.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.mockito.Mockito


fun Any.toJsonString(): String {
    return try {
        ObjectMapper().writeValueAsString(this)
    } catch (e: Exception) {
        throw RuntimeException(e)
    }
}

fun <T> anyObjectHelper(): T {
    return Mockito.any<T>()
}
