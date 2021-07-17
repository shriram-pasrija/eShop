package com.demo.eshop.da.entity

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "item_table")
class ItemEntity {

    @Id
    var id: Long = 0
    var name: String = ""
    var price: Double = 0.0
}
