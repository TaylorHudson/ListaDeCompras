package com.th.lista_de_compras.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.th.lista_de_compras.ui.component.FilterButton
import com.th.lista_de_compras.ui.component.SearchBar
import com.th.lista_de_compras.ui.component.ShoppingItemCard
import com.th.lista_de_compras.utils.formatAsCurrency
import com.th.lista_de_compras.viewmodel.FilterType
import com.th.lista_de_compras.viewmodel.ShoppingListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListDetailsScreen(
    navController: NavController,
    viewModel: ShoppingListViewModel,
    shoppingListId: String,
) {
    var searchQuery by remember { mutableStateOf("") }
    val shoppingList = viewModel.findShoppingListById(shoppingListId)
    val currentFilter = viewModel.filter

    if (shoppingList == null) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

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
                            tint = Color.Black,
                            contentDescription = "Voltar",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            Text(
                text = "Total gasto",
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 1.dp),
                style = TextStyle(
                    fontSize = 16.sp
                ),
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = shoppingList.totalPurchasedPrice().formatAsCurrency(),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                style = TextStyle(
                    fontSize = 22.sp
                ),
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )

            Button(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                onClick = {
                    navController.navigate("shopping-list/${shoppingList.id}/add-item")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Criar novo item",
                    modifier = Modifier.padding(1.dp),
                    style = TextStyle(
                        fontSize = 10.sp
                    ),
                )
            }

            if (shoppingList.items.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Nenhum item de compra cadastrado",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        Text(
                            text = "Toque no botão para criar um item de compra",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val buttonModifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp)
                        val filters = listOf(
                            FilterType.ALL to "Todos",
                            FilterType.PURCHASED to "Comprados",
                            FilterType.PENDING to "Pendentes"
                        )
                        Row {
                            filters.forEach { (type, label) ->
                                FilterButton(
                                    modifier = buttonModifier,
                                    onClick = { viewModel.setFilter(type) },
                                    text = label,
                                    selected = currentFilter.value == type
                                )
                            }
                        }
                    }

                    if (viewModel.findFilteredItems(shoppingList.id, searchQuery).isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Nenhum item encontrado",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                )

                                Spacer(modifier = Modifier.height(15.dp))

                                Text(
                                    text = "Toque no botão para criar um item de compra",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black,
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                        ) {

                            items(viewModel.findFilteredItems(shoppingList.id, searchQuery)) { shoppingItem ->
                                ShoppingItemCard(
                                    item = shoppingItem,
                                    onCheckedChange = { isChecked ->
                                        viewModel.toggleItemChecked(
                                            shoppingListId,
                                            shoppingItem.id,
                                            isChecked
                                        )
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
        }
    }
}