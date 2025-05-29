package com.example.grablist.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface ShopListDao{
    @Query("SELECT * FROM shoplist")
    fun getAll(): Flow<List<ShopList>>

    @Upsert
    suspend fun upsert(shopList: ShopList)

    @Delete
    suspend fun delete(shopList: ShopList)

}

@Dao
interface ProductDao{
    @Query("SELECT * FROM product")
    fun getAll(): Flow<List<Product>>

    @Upsert
    suspend fun upsert(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("SELECT * FROM product JOIN crossref ON product.productId = crossref.productId WHERE shopListId = :id")
    fun getProductsByListId(id: Int): Flow<List<Product>>

    @Query("SELECT * FROM product WHERE favorite = 1")
    fun getFavorites(): Flow<List<Product>>
}


@Dao
interface CrossRefDao{
    @Query("SELECT * FROM crossRef")
    fun getAll(): Flow<List<CrossRef>>
}