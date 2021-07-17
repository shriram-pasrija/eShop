package com.demo.eshop.da.entity

import javax.persistence.*

@Entity
@Table(name = "offers_table")
class OfferEntity {

    @Id
    @Column(name = "offer_id")
    var id = 0

    @OneToOne(targetEntity = ItemEntity::class)
    @JoinColumn(name = "item_id")
    var item: ItemEntity = ItemEntity()

    @Column(name = "offer_name")
    var name = ""

    @Column(name = "is_active")
    var isActive = false
}
