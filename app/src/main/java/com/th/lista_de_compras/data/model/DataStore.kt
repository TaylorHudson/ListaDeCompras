package com.th.lista_de_compras.data.model

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.th.lista_de_compras.viewmodel.SHOPPING_LISTS_KEY
import com.th.lista_de_compras.viewmodel.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val gson = Gson()

suspend fun saveShoppingLists(context: Context, lists: List<ShoppingList>) {
    val json = gson.toJson(lists)
    context.dataStore.edit { prefs ->
        prefs[SHOPPING_LISTS_KEY] = json
    }
}

fun loadShoppingLists(context: Context): Flow<List<ShoppingList>> {
    return context.dataStore.data.map { prefs ->
        val json = prefs[SHOPPING_LISTS_KEY] ?: ""
        if (json.isNotEmpty()) {
            try {
                val type = object : TypeToken<List<ShoppingList>>() {}.type
                gson.fromJson(json, type)
            } catch (e: Exception) {
                emptyList()
            }
        } else emptyList()
    }
}