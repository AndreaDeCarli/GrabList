package com.example.grablist.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.grablist.ui.screens.AddNewList
import com.example.grablist.ui.screens.AddNewProduct
import com.example.grablist.ui.screens.Favorites
import com.example.grablist.ui.screens.HomeScreen
import com.example.grablist.ui.screens.Profile
import com.example.grablist.ui.screens.ShopListDetailsScreen
import com.example.grablist.ui.viewmodels.AddNewProductViewModel
import com.example.grablist.ui.viewmodels.AddShopListViewModel
import com.example.grablist.ui.viewmodels.ProductsInShopListViewModel
import com.example.grablist.ui.viewmodels.ProductsViewModel
import com.example.grablist.ui.viewmodels.ShopListViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

sealed interface NavRoute {
    @Serializable data object HomeScreen : NavRoute
    @Serializable data object Favorites : NavRoute
    @Serializable data object Profile : NavRoute
    @Serializable data object AddNewList : NavRoute
    @Serializable data class AddNewProduct(val id: Long) : NavRoute
    @Serializable data class ShopListDetails(val id: Long) : NavRoute
}

@Composable
fun GrabListNavGraph(navController: NavHostController) {
    val shopListVm = koinViewModel<ShopListViewModel>()
    val shopListState by shopListVm.state.collectAsStateWithLifecycle()

    val productVm = koinViewModel<ProductsViewModel>()

    NavHost(navController = navController,
        startDestination = NavRoute.HomeScreen
    ) {
        composable<NavRoute.HomeScreen> {
            HomeScreen(navController, shopListVm, shopListState)
        }
        composable<NavRoute.ShopListDetails> { backStackEntry ->
            val route = backStackEntry.toRoute<NavRoute.ShopListDetails>()
            val shopList = requireNotNull(shopListState.shopLists.find { it.shopListId == route.id })

            val productsInListVm: ProductsInShopListViewModel = koinViewModel(parameters = { parametersOf(shopList.shopListId) })
            val state by productsInListVm.state.collectAsStateWithLifecycle()

            ShopListDetailsScreen(navController = navController, shopList = shopList, state = state, vm = productVm)

        }
        composable<NavRoute.Favorites> {
            Favorites(navController = navController,
                vm = productVm)
        }
        composable<NavRoute.Profile> {
            Profile(navController)
        }
        composable<NavRoute.AddNewList> {
            val addShopListVm = koinViewModel<AddShopListViewModel>()
            val state by addShopListVm.state.collectAsStateWithLifecycle()
            AddNewList(
                state,
                addShopListVm.actions,
                onSubmit = { shopListVm.addShopList(state.toShopList()) },
                navController
            )
        }

        composable<NavRoute.AddNewProduct> { backStackEntry ->
            val route = backStackEntry.toRoute<NavRoute.ShopListDetails>()

            val addProductVm = koinViewModel<AddNewProductViewModel>()
            val state by addProductVm.state.collectAsStateWithLifecycle()
            AddNewProduct(
                state,
                addProductVm.actions,
                onSubmit = {
                    if (route.id != -1L){
                        val shopList = requireNotNull(shopListState.shopLists.find { it.shopListId == route.id })
                        productVm.addProductAndReference(state.toProduct(), shopList)
                    }else{
                        productVm.addProduct(state.toProduct())
                    }
                           },
                navController
            )
        }
    }
}