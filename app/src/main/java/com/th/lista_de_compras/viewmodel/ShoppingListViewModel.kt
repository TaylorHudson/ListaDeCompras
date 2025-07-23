package com.th.lista_de_compras.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.th.lista_de_compras.data.model.ShoppingItem
import com.th.lista_de_compras.data.model.ShoppingList
import com.th.lista_de_compras.data.model.loadShoppingLists
import com.th.lista_de_compras.data.model.saveShoppingLists
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.Normalizer

enum class FilterType {
    ALL, PURCHASED, PENDING
}

class ShoppingListViewModel(application: Application) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
    private val shoppingLists = mutableStateListOf<ShoppingList>()
    private val _filter = mutableStateOf(FilterType.ALL)
    val filter: State<FilterType> = _filter

    init {
        viewModelScope.launch {
            loadShoppingLists(context).collect { loadedLists ->
                shoppingLists.clear()
                shoppingLists.addAll(loadedLists)
            }
        }
    }

    private fun persist() {
        viewModelScope.launch {
            saveShoppingLists(context, shoppingLists)
        }
    }

    fun setFilter(type: FilterType) {
        _filter.value = type
    }

    private fun removeAccents(input: String): String {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
    }

    fun findFilteredItems(listId: String, query: String): List<ShoppingItem> {
        val shoppingList = findShoppingListById(listId) ?: return emptyList()

        val normalizedQuery = removeAccents(query.lowercase())
        val filteredItems = if (query.isNotBlank()) {
            shoppingList.items.filter {
                val itemNameNormalized = removeAccents(it.name.lowercase())
                itemNameNormalized.contains(normalizedQuery)
            }
        } else {
            shoppingList.items
        }

        return when (filter.value) {
            FilterType.ALL -> filteredItems
            FilterType.PURCHASED -> filteredItems.filter { it.purchased }
            FilterType.PENDING -> filteredItems.filter { !it.purchased }
        }

    }

    fun addShoppingList(shoppingList: ShoppingList) {
        shoppingLists.add(shoppingList)
        persist()
    }

    fun deleteShoppingList(shoppingListId: String) {
        shoppingLists.removeAll { it.id == shoppingListId }
        persist()
    }

    fun updateShoppingList(shoppingList: ShoppingList) {
        val index = shoppingLists.indexOfFirst { it.id == shoppingList.id }
        shoppingLists[index] = shoppingList
        persist()
    }

    fun findShoppingListById(id: String): ShoppingList? {
        return shoppingLists.find { it.id == id }
    }

    fun findShoppingItemById(listId: String, itemId: String): ShoppingItem? {
        val list = shoppingLists[shoppingLists.indexOfFirst { it.id == listId }]
        return list.items.find { it.id == itemId }
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
        persist()
    }

    fun addShoppingItem(shoppingListId: String, item: ShoppingItem) {
        val listIndex = shoppingLists.indexOfFirst { it.id == shoppingListId }
        val list = shoppingLists[listIndex]
        list.addItem(item)
        shoppingLists[listIndex] = list
        persist()
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
        persist()
    }

    fun deleteShoppingItem(shoppingListId: String, itemId: String) {
        val listIndex = shoppingLists.indexOfFirst { it.id == shoppingListId }
        val list = shoppingLists[listIndex]

        val itemIndex = list.items.indexOfFirst { it.id == itemId }
        list.deleteItem(itemIndex)
        shoppingLists[listIndex] = list
        persist()
    }

}