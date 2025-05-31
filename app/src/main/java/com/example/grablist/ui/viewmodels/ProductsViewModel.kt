package com.example.grablist.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grablist.data.database.Product
import com.example.grablist.data.database.ShopList
import com.example.grablist.data.repositories.ShopListRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProductState(val products: List<Product>)

class ProductsViewModel(
    private val repository: ShopListRepository
) : ViewModel() {
    val state = repository.products.map { ProductState(products = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ProductState(emptyList())
    )

    fun addProduct(product: Product) = viewModelScope.launch {
        repository.addProduct(product)
    }

    fun deleteProduct(product: Product, shopList: ShopList) = viewModelScope.launch {
        repository.deleteProductFromList(shopList = shopList, product = product)
    }

    fun addProductAndReference(product: Product, shopList: ShopList) = viewModelScope.launch {
        repository.addProductAndReference(product, shopList)
    }

    fun changeFavorite(product: Product) = viewModelScope.launch {
        repository.changeFavoriteProduct(product)
    }

    val favorites = repository.getFavoriteProducts().map { ProductState(products = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ProductState(emptyList())
    )
}
