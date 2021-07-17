package com.demo.eshop.da.repo

import com.demo.eshop.da.entity.ItemEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepo : CrudRepository<ItemEntity, Long>