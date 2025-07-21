package com.th.lista_de_compras.ui.screen

import MoneyInputField
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.th.lista_de_compras.R
import com.th.lista_de_compras.data.model.ShoppingItem
import com.th.lista_de_compras.ui.component.ValidatedTextField
import com.th.lista_de_compras.utils.toBigDecimalFromCents
import com.th.lista_de_compras.utils.toCentsString
import com.th.lista_de_compras.viewmodel.ShoppingListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemScreen(
    shoppingListId: String,
    shoppingItemId: String,
    navController: NavController,
    viewModel: ShoppingListViewModel,
) {
    val shoppingItem = viewModel.findShoppingItemById(shoppingListId, shoppingItemId)

    if (shoppingItem == null) {
        LaunchedEffect(Unit) {
            navController.navigate("shopping-list/$shoppingListId/details")
        }
        return
    }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(shoppingItem.name) }
    var quantity by remember { mutableIntStateOf(shoppingItem.quantity) }
    var priceCents by remember { mutableStateOf(shoppingItem.price.toCentsString()) }

    fun saveItem() {
        val updatedItem = ShoppingItem(
            id = shoppingItem.id,
            name = name,
            quantity = quantity,
            price = priceCents.toBigDecimalFromCents()
        )
        viewModel.updateShoppingItem(
            shoppingListId = shoppingListId,
            item = updatedItem
        )
        navController.navigate("shopping-list/$shoppingListId/details")
    }

    fun deleteItem() {
        viewModel.deleteShoppingItem(shoppingListId, shoppingItem.id)
        navController.navigate("shopping-list/${shoppingListId}/details")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                        Text(
                            text = "Editar Item",
                            fontWeight = FontWeight.Bold
                        )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            saveItem()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = Color.Black,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { padding ->

        if (showDeleteDialog) {
            AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    textContentColor = Color.White,
                    title = { Text("Atenção") },
                    text = { Text("Você deseja excluir este item?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                deleteItem()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            )
                        ) {
                            Text("Sim")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showDeleteDialog = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            )
                        ) {
                            Text("Não")
                        }
                    }
                )
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Top
        ) {
            ValidatedTextField(
                value = name,
                onValueChange = { name = it },
                label = "Nome",
                validator = {
                    if (name.isBlank()) "Digite o nome do item" else null
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = quantity.toString(),
                    onValueChange = {
                        if (it.length <= 4) quantity = it.toIntOrNull() ?: 1
                    },
                    label = { Text("Quantidade") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        disabledTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        focusedIndicatorColor = Color.Black,
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = Color.Black
                    )
                )

                IconButton(
                    onClick = { if (quantity > 1) quantity-- },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_remove_24),
                        contentDescription = "Diminuir"
                    )
                }

                IconButton(
                    onClick = { quantity++ },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = "Aumentar"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            MoneyInputField(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .fillMaxWidth(0.6f),
                amount = priceCents,
                onAmountChange = { priceCents = it },
                label = "Preço"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showDeleteDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text("Deletar")
            }
        }
    }
}
