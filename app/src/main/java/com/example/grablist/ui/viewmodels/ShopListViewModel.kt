package com.example.grablist.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grablist.data.database.ShopList
import com.example.grablist.data.repositories.ShopListRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ShopListState(val shopLists: List<ShopList>)

class ShopListViewModel(
    private val repository: ShopListRepository
) : ViewModel() {
    val state = repository.shopLists.map { ShopListState(shopLists = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ShopListState(emptyList())
    )

    fun addShopList(shopList: ShopList) = viewModelScope.launch {
        repository.addShopList(shopList)
    }

    fun deleteShopList(shopList: ShopList) = viewModelScope.launch {
        repository.deleteShopList(shopList)
    }
}
