package com.th.lista_de_compras

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.th.lista_de_compras.ui.screen.ShoppingListDetailsScreen
import com.th.lista_de_compras.ui.screen.ShoppingListScreen
import com.th.lista_de_compras.viewmodel.ShoppingListViewModel

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    val viewModel = remember { ShoppingListViewModel() }

    NavHost(navController = navController, startDestination = "shopping-lists") {

        composable(
            route = "shopping-lists"
        ) {
            ShoppingListScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            route = "shopping-list-details/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getString("id")
            listId?.let {
                ShoppingListDetailsScreen(
                    navController = navController,
                    viewModel = viewModel,
                    shoppingListId = listId
                )
            }
        }

    }
}
