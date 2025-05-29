package com.example.grablist.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao{
    @Query("SELECT * FROM profile")
    fun getAll(): Flow<List<Profile>>

    @Upsert
    suspend fun upsert(profile: Profile)

    @Delete
    suspend fun delete(profile: Profile)

}

@Dao
interface ShopListDao{
    @Query("SELECT * FROM shoplist")
    fun getAll(): Flow<List<ShopList>>

    @Upsert
    suspend fun upsert(shopList: ShopList)

    @Delete
    suspend fun delete(shopList: ShopList)

    @Query("SELECT * FROM shopList WHERE profileId = :id")
    fun getShopListByProfile(id: Int): Flow<List<ShopList>>

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

    @Query("SELECT * FROM product WHERE profileId = :id AND favorite = 1")
    fun getFavoritesByProfile(id: Int): Flow<List<Product>>
}


@Dao
interface CrossRefDao{
    @Query("SELECT * FROM crossRef")
    fun getAll(): Flow<List<CrossRef>>
}