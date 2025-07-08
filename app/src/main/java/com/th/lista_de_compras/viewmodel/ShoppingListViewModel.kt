package com.th.lista_de_compras.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.th.lista_de_compras.data.model.ShoppingItem
import com.th.lista_de_compras.data.model.ShoppingList

enum class FilterType {
    ALL, PURCHASED, PENDING
}

class ShoppingListViewModel: ViewModel() {
    private val shoppingLists = mutableStateListOf<ShoppingList>()
    private val filter = mutableStateOf(FilterType.ALL)

    fun setFilter(type: FilterType) {
        filter.value = type
    }

    fun findFilteredItems(listId: String): List<ShoppingItem> {
        val shoppingList = findShoppingListById(listId)
        return when (filter.value) {
            FilterType.ALL -> shoppingList.items
            FilterType.PURCHASED -> shoppingList.items.filter { it.purchased }
            FilterType.PENDING -> shoppingList.items.filter { !it.purchased }
        }
    }

    fun addShoppingList(shoppingList: ShoppingList) {
        shoppingLists.add(shoppingList)
    }

    fun deleteShoppingList(shoppingListId: String) {
        shoppingLists.removeAll { it.id == shoppingListId }
    }

    fun updateShoppingList(shoppingList: ShoppingList) {
        val index = shoppingLists.indexOfFirst { it.id == shoppingList.id }
        shoppingLists[index] = shoppingList
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

    fun toggleItemChecked(shoppingListId: String, itemId: String, isChecked: Boolean) {
        val listIndex = shoppingLists.indexOfFirst { it.id == shoppingListId }
        val list = shoppingLists[listIndex]
        val updatedItems = list.items.map {
            if (it.id == itemId) it.copy(purchased = isChecked) else it
        }.toMutableList()
        shoppingLists[listIndex] = list.copy(items = updatedItems)
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