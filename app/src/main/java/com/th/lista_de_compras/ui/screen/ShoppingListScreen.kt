package com.th.lista_de_compras.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.th.lista_de_compras.data.model.ShoppingList
import com.th.lista_de_compras.ui.component.IconButtonWithText
import com.th.lista_de_compras.ui.component.ShoppingListCard
import com.th.lista_de_compras.viewmodel.ShoppingListViewModel
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    navController: NavController,
    viewModel: ShoppingListViewModel
) {
    val shoppingLists = viewModel.findShoppingLists()
    val scope = rememberCoroutineScope()

    val addShoppingListSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isAddShoppingListBottomSheetOpen by remember { mutableStateOf(false) }

    var newShoppingListName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Minhas listas",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {

            Button(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                onClick = {
                    isAddShoppingListBottomSheetOpen = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Criar nova lista",
                    modifier = Modifier.padding(1.dp),
                    style = TextStyle(
                        fontSize = 12.sp
                    ),
                )
            }

            if (shoppingLists.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Nenhuma lista de compra cadastrada",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        Text(
                            text = "Toque no botão para criar uma lista de compra",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                        )
                    }
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
                        onCardClick = {
                            navController.navigate("shopping-list/${shoppingList.id}/details")
                        },
                        onManageListClick = { navController.navigate("shopping-list/${shoppingList.id}/manage") },
                    )
                }
            }

            if (isAddShoppingListBottomSheetOpen) {
                ModalBottomSheet(
                    onDismissRequest = {
                        isAddShoppingListBottomSheetOpen = false
                        newShoppingListName = ""
                    },
                    sheetState = addShoppingListSheetState,
                    containerColor = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .background(Color.White),
                    ) {
                        TextField(
                            value = newShoppingListName,
                            onValueChange = { newShoppingListName = it },
                            label = { Text("Nome da lista") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
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

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                viewModel.addShoppingList(ShoppingList(UUID.randomUUID().toString(), newShoppingListName))
                                newShoppingListName = ""
                                scope.launch {
                                    addShoppingListSheetState.hide()
                                    isAddShoppingListBottomSheetOpen = false
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Salvar")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}