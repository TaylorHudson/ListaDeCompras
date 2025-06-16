package com.th.lista_de_compras.ui.screen

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.th.lista_de_compras.data.model.ShoppingList
import com.th.lista_de_compras.ui.component.IconButtonWithText
import com.th.lista_de_compras.viewmodel.ShoppingListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(viewModel: ShoppingListViewModel) {
    val shoppingLists = viewModel.findShoppingLists()
    val scope = rememberCoroutineScope()
    val addShoppingListSheetState = rememberModalBottomSheetState()
    val manageShoppingListSheetState = rememberModalBottomSheetState()
    var isAddShoppingListBottomSheetOpen by remember { mutableStateOf(false) }
    var isManageShoppingListBottomSheetOpen by remember { mutableStateOf(false) }
    var newShoppingListName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Minhas listas",
                    fontWeight = FontWeight.Bold
                )
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isAddShoppingListBottomSheetOpen = true },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add new shopping list")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {

            if (shoppingLists.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Nenhuma lista de compra cadastrada",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        text = "Toque no botão \"+\" para criar uma lista de compra",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(shoppingLists) { shoppingList ->
                    ShoppingListCard(
                        shoppingList = shoppingList,
                        onManageListClick = { isManageShoppingListBottomSheetOpen = true },
                    )
                }
            }

            if (isAddShoppingListBottomSheetOpen) {
                ModalBottomSheet(
                    onDismissRequest = {
                        isAddShoppingListBottomSheetOpen = false
                        newShoppingListName = ""
                    },
                    sheetState = addShoppingListSheetState
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        TextField(
                            value = newShoppingListName,
                            onValueChange = { newShoppingListName = it },
                            label = { Text("Nome da lista") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                viewModel.addShoppingList(ShoppingList(newShoppingListName))
                                newShoppingListName = ""
                                scope.launch {
                                    addShoppingListSheetState.hide()
                                    isAddShoppingListBottomSheetOpen = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Adicionar")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            if (isManageShoppingListBottomSheetOpen) {
                ModalBottomSheet(
                    onDismissRequest = {
                        isManageShoppingListBottomSheetOpen = false
                    },
                    sheetState = manageShoppingListSheetState
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        IconButtonWithText(
                            text = "Renomear",
                            icon = Icons.Default.Edit,
                            onClick = { /* ... */ }
                        )

                        IconButtonWithText(
                            text = "Excluir",
                            textStyle = TextStyle(
                                color = Color.Red
                            ),
                            isDeleteButton = true,
                            icon = Icons.Default.Delete,
                            onClick = { /* ... */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShoppingListCard(shoppingList: ShoppingList, onManageListClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = shoppingList.name,
                style = MaterialTheme.typography.titleLarge
            )

            IconButton(
                onClick = onManageListClick
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Manage list",
                )
            }
        }
    }
}
