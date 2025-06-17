package com.th.lista_de_compras.data.model

import java.math.BigDecimal
import java.util.UUID

data class ShoppingItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val quantity: Int = 1,
    val price: BigDecimal,
    val purchased: Boolean = false
) {
    fun togglePurchased() = copy(purchased = !purchased)
}

data class ShoppingList(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val items: MutableList<ShoppingItem> = mutableListOf()
) {

    fun totalPrice(): BigDecimal {
        return items.fold(BigDecimal.ZERO) { accumulator, item -> accumulator + (item.price * BigDecimal(item.quantity)) }
    }

    fun addItem(item: ShoppingItem) {
        if (item.name.isNotBlank() && items.none { it.name == item.name }) {
            items.add(item)
        }
    }
}