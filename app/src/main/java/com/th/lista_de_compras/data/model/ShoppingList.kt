package com.th.lista_de_compras.data.model

import java.math.BigDecimal
import java.util.UUID

data class ShoppingItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val quantity: Int = 1,
    val price: BigDecimal,
    val purchased: Boolean = false
)

data class ShoppingList(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val items: MutableList<ShoppingItem> = mutableListOf()
) {

    fun totalPurchasedPrice(): BigDecimal {
        return items.fold(BigDecimal.ZERO) { accumulator, item -> accumulator + (if (item.purchased) item.price * BigDecimal(item.quantity) else BigDecimal.ZERO) }
    }

    fun addItem(item: ShoppingItem) {
        if (item.name.isNotBlank() && items.none { it.name == item.name }) {
            items.add(item)
        }
    }

    fun updateItem(itemIndex: Int, item: ShoppingItem) {
        if (item.name.isNotBlank() && items.none { it.name == item.name }) {
            items[itemIndex] = item
        }
    }

    fun deleteItem(itemIndex: Int) {
        if (itemIndex in items.indices) {
            items.removeAt(itemIndex)
        }
    }
}