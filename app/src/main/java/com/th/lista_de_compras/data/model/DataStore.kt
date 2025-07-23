package com.th.lista_de_compras.data.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_data")
private val SHOPPING_LISTS_KEY = stringPreferencesKey("shopping_lists")
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
