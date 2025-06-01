package com.example.grablist.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
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
    suspend fun upsert(product: Product): Long

    @Delete
    suspend fun delete(product: Product)

    @Query("SELECT * FROM product WHERE productId = :id")
    fun getProductById(id: Long): Flow<Product>

    @Query("SELECT * FROM product JOIN crossref ON product.productId = crossref.productId WHERE shopListId = :id")
    fun getProductsByListId(id: Long): Flow<List<Product>>

    @Query("SELECT * FROM product WHERE favorite = 1")
    fun getFavorites(): Flow<List<Product>>
}


@Dao
interface CrossRefDao{
    @Query("SELECT * FROM crossRef")
    fun getAll(): Flow<List<CrossRef>>

    @Upsert
    suspend fun upsert(crossRef: CrossRef)

    @Delete
    suspend fun delete(crossRef: CrossRef)

    @Query("SELECT * FROM crossref WHERE shopListId = :id")
    fun getCrossRefById(id: Long): Flow<List<CrossRef>>

    @Query("SELECT * FROM crossref WHERE productId = :id")
    fun getListsFromProduct(id: Long): Flow<List<CrossRef>>
}