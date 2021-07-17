package com.demo.eshop.da.entity

import com.demo.eshop.dto.response.AvailableItemResponseDto
import com.demo.eshop.dto.response.OrderSummaryResponse
import javax.persistence.*

@Entity
@Table(name = "order_table")
class OrderEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "order_id")
    var id: Long = 0

    @OneToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "order_item_mapping_table",
        joinColumns = [JoinColumn(name = "order_id")],
        inverseJoinColumns = [JoinColumn(name = "order_item_id")]
    )
    var items: MutableList<OrderItemEntity> = mutableListOf()

    @Column(name = "order_amount")
    var amount = 0.0
}

fun OrderEntity.toOrderSummaryResponse(): OrderSummaryResponse {
    this.let {
        return OrderSummaryResponse().apply {
            this.id = it.id
            this.items =
                it.items.map { o ->
                    AvailableItemResponseDto(
                        o.item.id,
                        o.item.name,
                        o.quantity,
                        o.item.price,
                        o.amount,
                        o.offer?.name
                    )
                }
            totalAmount = it.amount
        }
    }
}
