package com.th.lista_de_compras.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.th.lista_de_compras.ui.component.ShoppingItemCard
import com.th.lista_de_compras.utils.formatAsCurrency
import com.th.lista_de_compras.viewmodel.ShoppingListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListDetailsScreen(
    navController: NavController,
    viewModel: ShoppingListViewModel,
    shoppingListId: String,
) {
    val shoppingList = viewModel.findShoppingListById(shoppingListId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = shoppingList.name,
                        fontWeight = FontWeight.Bold,
                    )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar"
                    )
                }
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("shopping-list/${shoppingList.id}/add-item")
                },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add new shopping list item")
            }
        },
    )  { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "Total: ${shoppingList.totalPurchasedPrice().formatAsCurrency()}",
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .align(Alignment.Start),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(shoppingList.items) { shoppingItem ->
                    ShoppingItemCard(
                        item = shoppingItem,
                        onCheckedChange = { isChecked ->
                            viewModel.toggleItemChecked(shoppingListId, shoppingItem.id, isChecked)
                        },
                        onCardClick = {
                            navController.navigate("shopping-list/${shoppingList.id}/edit-item/${shoppingItem.id}")
                        }
                    )
                }
            }
        }

    }
}