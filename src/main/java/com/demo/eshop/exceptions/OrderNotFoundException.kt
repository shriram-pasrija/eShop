package com.demo.eshop.exceptions

import java.lang.RuntimeException

class OrderNotFoundException : RuntimeException("Order with supplied ID does not exist")