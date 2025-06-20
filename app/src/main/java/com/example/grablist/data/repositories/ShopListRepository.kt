package com.example.grablist.data.repositories

import android.content.Context
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import com.example.grablist.data.database.CrossRef
import com.example.grablist.data.database.CrossRefDao
import com.example.grablist.data.database.Product
import com.example.grablist.data.database.ProductDao
import com.example.grablist.data.database.ShopList
import com.example.grablist.data.database.ShopListDao
import com.example.grablist.utils.deleteImageFromFiles
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ShopListRepository(
    private val shopListDao: ShopListDao,
    private val productDao: ProductDao,
    private val crossRefDao: CrossRefDao,
    private val ctx: Context
) {
    val shopLists: Flow<List<ShopList>> = shopListDao.getAll()

    val products: Flow<List<Product>> = productDao.getAll()

    private val crossRef: Flow<List<CrossRef>> = crossRefDao.getAll()

    suspend fun addShopList(shopList: ShopList){
        shopListDao.upsert(shopList)
    }

    suspend fun deleteShopList(shopList: ShopList) {
        val productsInShopList = productDao.getProductsByListId(shopList.shopListId)
        val references = crossRefDao.getCrossRefById(shopList.shopListId)
        productsInShopList.first().forEach {it -> if (!it.favorite) {
            deleteProduct(it)
        }}

        references.first().forEach{it -> crossRefDao.delete(it)}

        shopListDao.delete(shopList)

    }

    suspend fun changeFavoriteProduct(product: Product) {
        val productToChange = productDao.getProductById(product.productId).first()
        val references = crossRefDao.getListsFromProduct(productToChange.productId).first()
        if (references.isEmpty() && productToChange.favorite){
            deleteProduct(productToChange)
        }else{
            val newProduct = Product(
                productId = productToChange.productId,
                name = productToChange.name,
                imageUri = productToChange.imageUri,
                favorite = !productToChange.favorite)
            productDao.upsert(newProduct)
        }
    }

    fun getProductsByShopListId(id: Long) : Flow<List<Product>>{
        return productDao.getProductsByListId(id)
    }

    fun getFavoriteProducts() : Flow<List<Product>>{
        return productDao.getFavorites()
    }

    suspend fun deleteProductFromList(product: Product, shopList: ShopList){
        val crossRefToDelete = requireNotNull(
            crossRef.first().find { it.shopListId == shopList.shopListId && it.productId == product.productId })
        crossRefDao.delete(crossRefToDelete)

        if (!product.favorite && crossRef.first().find { it.productId == product.productId } == null) {
            deleteProduct(product)
        }
    }

    suspend fun addProduct(product: Product) {
        productDao.upsert(product)
    }

    suspend fun addProductAndReference(product: Product, shopList: ShopList){
        val id = productDao.upsert(product)
        val shopListId = shopList.shopListId
        crossRefDao.upsert(CrossRef(shopListId, id))
    }

    suspend fun addReference(product: Product, shopList: ShopList){
        crossRefDao.upsert(CrossRef(shopList.shopListId, product.productId))
    }

    private suspend fun deleteProduct(product: Product){
        deleteImageFromFiles(ctx, Uri.parse(product.imageUri))
        productDao.delete(product)
    }
}