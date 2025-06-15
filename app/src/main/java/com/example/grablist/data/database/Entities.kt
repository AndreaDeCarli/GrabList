package com.example.grablist.data.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Location(
    val name: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0
)

@Entity
data class ShopList(
    @PrimaryKey(autoGenerate = true)
    val shopListId: Long = 0,

    @ColumnInfo
    val title: String,

    @ColumnInfo
    val iconId: Long,

    @ColumnInfo
    val date: String,

    @Embedded
    val location: Location
)

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true)
    val productId: Long = 0,

    @ColumnInfo
    val name: String,

    @ColumnInfo
    val imageUri: String,

    @ColumnInfo
    val favorite: Boolean
)


@Entity(primaryKeys = ["shopListId", "productId"])
data class CrossRef(
    val shopListId: Long,
    val productId: Long
)