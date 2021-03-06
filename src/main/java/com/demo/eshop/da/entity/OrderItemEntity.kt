package com.demo.eshop.da.entity

import javax.persistence.*

@Entity
class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_item_id")
    var id = 0

    @OneToOne(targetEntity = ItemEntity::class)
    var item: ItemEntity = ItemEntity()
    var quantity: Int = 0

    @OneToOne(targetEntity = OfferEntity::class)
    @JoinColumn(name = "offer_id")
    var offer: OfferEntity? = null

    @Column(name = "amount")
    var amount: Double = 0.0
}
