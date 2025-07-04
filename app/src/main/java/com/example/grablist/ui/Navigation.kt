package com.example.grablist.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.grablist.ui.screens.ActiveShoppingScreen
import com.example.grablist.ui.screens.AddNewList
import com.example.grablist.ui.screens.AddNewProduct
import com.example.grablist.ui.screens.ChooseFavoriteScreen
import com.example.grablist.ui.screens.Favorites
import com.example.grablist.ui.screens.HomeScreen
import com.example.grablist.ui.screens.ProductDetailsScreen
import com.example.grablist.ui.screens.Profile
import com.example.grablist.ui.screens.SettingsScreen
import com.example.grablist.ui.screens.ShopListDetailsScreen
import com.example.grablist.ui.viewmodels.AddNewProductViewModel
import com.example.grablist.ui.viewmodels.AddShopListViewModel
import com.example.grablist.ui.viewmodels.ProductsInShopListViewModel
import com.example.grablist.ui.viewmodels.ProductsViewModel
import com.example.grablist.ui.viewmodels.SettingsState
import com.example.grablist.ui.viewmodels.SettingsViewModel
import com.example.grablist.ui.viewmodels.ShopListViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

sealed interface NavRoute {
    @Serializable data object HomeScreen : NavRoute
    @Serializable data object Favorites : NavRoute
    @Serializable data object Profile : NavRoute
    @Serializable data object AddNewList : NavRoute
    @Serializable data object Settings : NavRoute
    @Serializable data class AddNewProduct(val id: Long, val lockFavorite: Boolean) : NavRoute
    @Serializable data class ShopListDetails(val id: Long) : NavRoute
    @Serializable data class ProductDetails(val id: Long) : NavRoute
    @Serializable data class ChooseFavProduct(val id: Long) : NavRoute
    @Serializable data class ActiveShopping(val id: Long) : NavRoute
}

@Composable
fun GrabListNavGraph(navController: NavHostController, settingsViewModel: SettingsViewModel, settingsState: SettingsState) {

    val ctx = LocalContext.current

    val shopListVm = koinViewModel<ShopListViewModel>()
    val shopListState by shopListVm.state.collectAsStateWithLifecycle()

    val productVm = koinViewModel<ProductsViewModel>()
    val productState by productVm.state.collectAsStateWithLifecycle()

    NavHost(navController = navController,
        startDestination = NavRoute.HomeScreen
    ) {
        composable<NavRoute.HomeScreen> {
            HomeScreen(navController, { shopList -> shopListVm.deleteShopList(shopList) }, shopListState)
        }

        composable<NavRoute.ShopListDetails> { backStackEntry ->
            val route = backStackEntry.toRoute<NavRoute.ShopListDetails>()
            val shopList = requireNotNull(shopListState.shopLists.find { it.shopListId == route.id })

            val productsInListVm: ProductsInShopListViewModel = koinViewModel(parameters = { parametersOf(shopList.shopListId) })
            val state by productsInListVm.state.collectAsStateWithLifecycle()

            ShopListDetailsScreen(navController = navController, shopList = shopList, state = state, vm = productVm)
        }

        composable<NavRoute.ActiveShopping> { backStackEntry ->
            val route = backStackEntry.toRoute<NavRoute.ActiveShopping>()
            val shopList = requireNotNull(shopListState.shopLists.find { it.shopListId == route.id })

            val productsInListVm: ProductsInShopListViewModel = koinViewModel(parameters = { parametersOf(shopList.shopListId) })
            val state by productsInListVm.state.collectAsState()

            ActiveShoppingScreen(
                navController = navController,
                state = state,
                shopList = shopList,
                onCompleteList = {
                    settingsViewModel.increaseProgress()
                    settingsViewModel.setLatestProgressDate()
                })
        }

        composable<NavRoute.ProductDetails> { backStackEntry ->
            val route = backStackEntry.toRoute<NavRoute.ProductDetails>()
            val product = requireNotNull(productState.products.find { it.productId == route.id })

            ProductDetailsScreen(navController = navController, product = product)
        }
        
        composable<NavRoute.Favorites> {
            Favorites(navController = navController,
                vm = productVm)
        }

        composable<NavRoute.Profile> {
            val productsFavorites by productVm.favorites.collectAsStateWithLifecycle()
            Profile(navController,
                settingsState = settingsState,
                shopListState = shopListState,
                productState = productsFavorites)
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
            val route = backStackEntry.toRoute<NavRoute.AddNewProduct>()

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
                navController = navController,
                lockFavorites = route.lockFavorite,
                ctx = ctx
            )
        }

        composable<NavRoute.ChooseFavProduct> { backStackEntry ->
            val route = backStackEntry.toRoute<NavRoute.ShopListDetails>()
            val shopList = requireNotNull(shopListState.shopLists.find { it.shopListId == route.id })

            val productsInListVm: ProductsInShopListViewModel = koinViewModel(parameters = { parametersOf(shopList.shopListId) })
            val state by productsInListVm.state.collectAsState()

            ChooseFavoriteScreen(
                navController = navController,
                vm = productVm,
                products = state.products,
                shopList = shopList
            )
        }

        composable<NavRoute.Settings> {
            SettingsScreen(navController, settingsState, settingsViewModel)
        }
    }
}