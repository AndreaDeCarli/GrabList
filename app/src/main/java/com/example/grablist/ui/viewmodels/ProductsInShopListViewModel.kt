package com.example.grablist.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grablist.data.database.Product
import com.example.grablist.data.repositories.ShopListRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ProductsInListState(val products: List<Product>)

class ProductsInShopListViewModel(
    private val shopListId: Long,
    private val repository: ShopListRepository): ViewModel()
{
    val state = repository.getProductsByShopListId(shopListId).map { ProductsInListState(products = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ProductsInListState(emptyList())
    )


}