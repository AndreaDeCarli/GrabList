package com.example.grablist.data.repositories

import com.example.grablist.data.database.CrossRef
import com.example.grablist.data.database.CrossRefDao
import com.example.grablist.data.database.Product
import com.example.grablist.data.database.ProductDao
import com.example.grablist.data.database.ShopList
import com.example.grablist.data.database.ShopListDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ShopListRepository(
    private val shopListDao: ShopListDao,
    private val productDao: ProductDao,
    private val crossRefDao: CrossRefDao
) {
    val shopLists: Flow<List<ShopList>> = shopListDao.getAll()

    val products: Flow<List<Product>> = productDao.getAll()

    val crossRef: Flow<List<CrossRef>> = crossRefDao.getAll()

    suspend fun addShopList(shopList: ShopList){
        shopListDao.upsert(shopList)
    }

    suspend fun deleteShopList(shopList: ShopList) {
        val productsInShopList = productDao.getProductsByListId(shopList.shopListId)
        val references = crossRefDao.getCrossRefById(shopList.shopListId)
        productsInShopList.first().forEach {it -> if (!it.favorite) {
            productDao.delete(it)
        }}

        references.first().forEach{it -> crossRefDao.delete(it)}

        shopListDao.delete(shopList)

    }

    suspend fun changeFavoriteProduct(product: Product) {
        val productToChange = productDao.getProductById(product.productId).first()
        val newProduct = Product(
            productId = productToChange.productId,
            name = productToChange.name,
            imageUri = productToChange.imageUri,
            favorite = !productToChange.favorite)
        productDao.upsert(newProduct)
    }

    fun getProductsByShopListId(id: Long) : Flow<List<Product>>{
        return productDao.getProductsByListId(id)
    }

    suspend fun deleteProductFromList(product: Product, shopList: ShopList){
        if (!product.favorite) {
            productDao.delete(product)
        }
        val crossRefToDelete = requireNotNull(
            crossRef.first().find { it.shopListId == shopList.shopListId && it.productId == product.productId })
        crossRefDao.delete(crossRefToDelete)
    }


}