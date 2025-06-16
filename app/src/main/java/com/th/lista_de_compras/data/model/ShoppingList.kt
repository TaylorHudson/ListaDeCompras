package com.th.lista_de_compras.data.model

import java.math.BigDecimal

data class ShoppingItem(
    val name: String,
    val quantity: Int = 1,
    val price: BigDecimal,
    val purchased: Boolean = false
)

data class ShoppingList(
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