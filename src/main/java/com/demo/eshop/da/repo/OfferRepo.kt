package com.demo.eshop.da.repo

import com.demo.eshop.da.entity.OfferEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OfferRepo : CrudRepository<OfferEntity, Int> {
    fun findByItemId(id: Long): Optional<OfferEntity>
}
