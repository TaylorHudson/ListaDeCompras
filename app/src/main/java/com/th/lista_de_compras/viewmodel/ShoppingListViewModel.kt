package com.th.lista_de_compras.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.th.lista_de_compras.data.model.ShoppingItem
import com.th.lista_de_compras.data.model.ShoppingList
import java.math.BigDecimal

class ShoppingListViewModel: ViewModel() {
    private val shoppingLists = mutableStateListOf<ShoppingList>()

//    init {
//        val shoppingList = ShoppingList("Feira")
//        addShoppingList(shoppingList)
//        shoppingList.addItem(ShoppingItem(name = "Arroz", price = BigDecimal.valueOf(8.50)))
//        shoppingList.addItem(ShoppingItem(name = "Sal", price = BigDecimal.valueOf(2.00)))
//    }

    fun findShoppingLists(): List<ShoppingList> {
        return shoppingLists
    }

    fun addShoppingList(shoppingList: ShoppingList) {
        if (shoppingList.name.isNotBlank() && shoppingLists.none { it.name == shoppingList.name }) {
            shoppingLists.add(shoppingList)
        }
    }

    fun removeShoppingList(shoppingList: ShoppingList) {
        shoppingLists.remove(shoppingList)
    }

    fun updateShoppingListName(shoppingList: ShoppingList, newName: String) {
        if (newName.isNotBlank() && shoppingLists.none { it.name == newName }) {
            val index = shoppingLists.indexOf(shoppingList)
            if (index != -1) {
                shoppingLists[index] = shoppingList.copy(name = newName.trim())
            }
        }
    }
}