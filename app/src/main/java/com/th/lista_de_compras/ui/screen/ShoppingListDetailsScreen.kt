package com.th.lista_de_compras.ui.screen

import android.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    )  { paddingValues ->

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
                        fontSize = 12.sp
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
}