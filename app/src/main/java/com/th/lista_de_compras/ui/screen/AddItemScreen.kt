package com.th.lista_de_compras.ui.screen

import MoneyInputField
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.th.lista_de_compras.R
import com.th.lista_de_compras.data.model.ShoppingItem
import com.th.lista_de_compras.utils.toBigDecimalFromCents
import com.th.lista_de_compras.viewmodel.ShoppingListViewModel
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    shoppingListId: String,
    navController: NavController,
    viewModel: ShoppingListViewModel,
) {
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(1) }
    var priceCents by remember { mutableStateOf("") }

    fun resetStates() {
        name = ""
        quantity = 1
        priceCents = ""
    }

    fun onAddItemClick() {
        val item = ShoppingItem(
            name = name,
            price = priceCents.toBigDecimalFromCents(),
            quantity = quantity
        )
        viewModel.addShoppingItem(
            shoppingListId = shoppingListId,
            item = item
        )
        navController.navigate("shopping-list/$shoppingListId/details")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                        Text(
                            text = "Adicionar Item",
                            fontWeight = FontWeight.Bold
                        )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
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
                        .weight(1f)
                )

                IconButton(
                    onClick = { if (quantity > 1) quantity-- }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_remove_24),
                        contentDescription = "Diminuir"
                    )
                }

                IconButton(
                    onClick = { quantity++ }
                ) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = "Aumentar"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            MoneyInputField(
                amount = priceCents,
                onAmountChange = { priceCents = it },
                label = "Preço"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onAddItemClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar")
            }
        }
    }
}
