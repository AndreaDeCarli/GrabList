package com.example.grablist.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ShopList::class, Product::class, CrossRef::class], version = 1)
abstract class ShopListDatabase: RoomDatabase() {
    abstract fun shopListDAO(): ShopListDao
    abstract fun productDAO(): ProductDao
    abstract fun crossRefDAO(): CrossRefDao
}