package com.th.lista_de_compras.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.th.lista_de_compras.data.model.ShoppingItem
import com.th.lista_de_compras.data.model.ShoppingList
import java.math.BigDecimal
import java.util.UUID

class ShoppingListViewModel: ViewModel() {
    private val shoppingLists = mutableStateListOf<ShoppingList>()

    init {
        val shoppingList = ShoppingList(id = UUID.randomUUID().toString(), name = "Feira do Mês")
        addShoppingList(shoppingList)
        shoppingList.addItem(ShoppingItem(name = "Arroz", price = BigDecimal.valueOf(8.50)))
        shoppingList.addItem(ShoppingItem(name = "Sal", price = BigDecimal.valueOf(2.00)))
    }

    fun findShoppingListById(id: String): ShoppingList? {
        return shoppingLists.find { it.id == id }
    }

    fun findShoppingLists(): List<ShoppingList> {
        return shoppingLists
    }

    fun addShoppingList(shoppingList: ShoppingList) {
        if (shoppingList.name.isNotBlank() && shoppingLists.none { it.name == shoppingList.name }) {
            shoppingLists.add(shoppingList)
        }
    }

    fun toggleItemChecked(shoppingListId: String, itemId: String, isChecked: Boolean) {
        val listIndex = shoppingLists.indexOfFirst { it.id == shoppingListId }
        val list = shoppingLists[listIndex]
        val updatedItems = list.items.map { item ->
            if (item.id == itemId) item.copy(purchased = isChecked) else item
        }
        val updatedList = list.copy(items = updatedItems.toMutableList())
        shoppingLists[listIndex] = updatedList
    }

}