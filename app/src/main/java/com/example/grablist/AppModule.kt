package com.example.grablist

import android.content.Context
import androidx.room.Room
import com.example.grablist.data.database.CrossRefDao
import com.example.grablist.data.database.ShopListDatabase
import com.example.grablist.data.repositories.ShopListRepository
import com.example.grablist.ui.viewmodels.ShopListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(
            get(),
            ShopListDatabase::class.java,
            "shop-list"
        ).fallbackToDestructiveMigration()
            .build()
    }

    single {
        ShopListRepository(
            get<ShopListDatabase>().shopListDAO(),
            get<ShopListDatabase>().productDAO(),
            get<ShopListDatabase>().crossRefDAO()
        )
    }

    viewModel { ShopListViewModel(get()) }

}