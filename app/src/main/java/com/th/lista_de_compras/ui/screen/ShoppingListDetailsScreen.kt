package com.th.lista_de_compras.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.th.lista_de_compras.ui.component.CurrencyInputField
import com.th.lista_de_compras.ui.component.ShoppingItemCard
import com.th.lista_de_compras.viewmodel.ShoppingListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListDetailsScreen(
    navController: NavController,
    viewModel: ShoppingListViewModel,
    shoppingListId: String
) {
    val shoppingList = viewModel.findShoppingListById(shoppingListId)
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf(1) }
    var amount by remember { mutableStateOf("") }

    var isAddItemBottomSheetOpen by remember { mutableStateOf(false) }
    val addItemBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to shopping lists",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { navController.popBackStack() }
                    )

                    shoppingList?.name?.let {
                        Text(
                            text = it,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isAddItemBottomSheetOpen = true },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add new shopping list item")
            }
        },
    )  { paddingValues ->

        Box(
            modifier = Modifier.padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                shoppingList?.items?.let {
                    items(shoppingList.items) { shoppingItem ->
                        ShoppingItemCard(
                            item = shoppingItem,
                            onCheckedChange = { isChecked ->
                                viewModel.toggleItemChecked(shoppingListId, shoppingItem.id, isChecked)
                            }
                        )
                    }
                }
            }

            if (isAddItemBottomSheetOpen) {
                ModalBottomSheet(
                    onDismissRequest = {
                        isAddItemBottomSheetOpen = false
                    },
                    sheetState = addItemBottomSheetState
                ) {
                    CurrencyInputField(
                        valueInCents = amount,
                        onValueChange = { amount = it },
                        label = "Preço",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

        }

    }
}