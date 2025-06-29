package com.th.lista_de_compras.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.th.lista_de_compras.data.model.ShoppingItem
import com.th.lista_de_compras.data.model.ShoppingList
import java.math.BigDecimal
import java.util.UUID

class ShoppingListViewModel: ViewModel() {
    private val shoppingLists = mutableStateListOf<ShoppingList>()
    var wasListDeleted by mutableStateOf(false)

    init {
        val shoppingList = ShoppingList(id = UUID.randomUUID().toString(), name = "Feira do Mês")
        addShoppingList(shoppingList)
        shoppingList.addItem(ShoppingItem(name = "Arroz", price = BigDecimal.valueOf(8.50)))
        shoppingList.addItem(ShoppingItem(name = "Sal", price = BigDecimal.valueOf(2.00)))
    }

    fun findShoppingListById(id: String): ShoppingList {
        return shoppingLists[shoppingLists.indexOfFirst { it.id == id }]
    }

    fun findShoppingItemById(listId: String, itemId: String): ShoppingItem {
        val list = shoppingLists[shoppingLists.indexOfFirst { it.id == listId }]
        return list.items.first { it.id == itemId }
    }

    fun findShoppingLists(): List<ShoppingList> {
        return shoppingLists
    }

    fun addShoppingList(shoppingList: ShoppingList) {
        if (shoppingList.name.isNotBlank() && shoppingLists.none { it.name == shoppingList.name }) {
            shoppingLists.add(shoppingList)
        }
    }

    fun updateShoppingList(shoppingList: ShoppingList) {
        val index = shoppingLists.indexOfFirst { it.id == shoppingList.id }
        shoppingLists[index] = shoppingList
    }

    fun deleteShoppingList(shoppingListId: String) {
        val index = shoppingLists.indexOfFirst { it.id == shoppingListId }
        shoppingLists.removeAt(index)
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

    fun addShoppingItem(shoppingListId: String, item: ShoppingItem) {
        val listIndex = shoppingLists.indexOfFirst { it.id == shoppingListId }
        val list = shoppingLists[listIndex]
        list.addItem(item)
        shoppingLists[listIndex] = list
    }

    fun updateShoppingItem(shoppingListId: String, item: ShoppingItem) {
        val listIndex = shoppingLists.indexOfFirst { it.id == shoppingListId }
        val list = shoppingLists[listIndex]

        val itemIndex = list.items.indexOfFirst { it.id == item.id }
        val oldItem = list.items[itemIndex]

        val updatedItem = item.copy(id = oldItem.id, purchased = oldItem.purchased)
        list.updateItem(itemIndex, updatedItem)
        val updatedList = list.copy(items = list.items.toMutableList().apply { set(itemIndex, updatedItem) })
        shoppingLists[listIndex] = updatedList

    }

    fun deleteShoppingItem(shoppingListId: String, itemId: String) {
        val listIndex = shoppingLists.indexOfFirst { it.id == shoppingListId }
        val list = shoppingLists[listIndex]

        val itemIndex = list.items.indexOfFirst { it.id == itemId }
        list.deleteItem(itemIndex)
        shoppingLists[listIndex] = list
    }

}