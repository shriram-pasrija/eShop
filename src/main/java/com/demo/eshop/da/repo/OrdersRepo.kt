package com.demo.eshop.da.repo

import com.demo.eshop.da.entity.OrderEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrdersRepo : CrudRepository<OrderEntity, Long>