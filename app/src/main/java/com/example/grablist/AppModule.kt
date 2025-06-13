package com.example.grablist

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.grablist.data.database.CrossRefDao
import com.example.grablist.data.database.ShopListDatabase
import com.example.grablist.data.repositories.SettingsRepository
import com.example.grablist.data.repositories.ShopListRepository
import com.example.grablist.ui.viewmodels.AddNewProductViewModel
import com.example.grablist.ui.viewmodels.AddShopListViewModel
import com.example.grablist.ui.viewmodels.ProductsInShopListViewModel
import com.example.grablist.ui.viewmodels.ProductsViewModel
import com.example.grablist.ui.viewmodels.SettingsViewModel
import com.example.grablist.ui.viewmodels.ShopListViewModel
import com.example.grablist.utils.OSMDataSource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("settings")

val appModule = module {

    single { get<Context>().dataStore }

    single {
        Room.databaseBuilder(
            get(),
            ShopListDatabase::class.java,
            "shop-list"
        ).fallbackToDestructiveMigration()
            .build()
    }

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    single {
        ShopListRepository(
            get<ShopListDatabase>().shopListDAO(),
            get<ShopListDatabase>().productDAO(),
            get<ShopListDatabase>().crossRefDAO(),
            get<Context>()
        )
    }

    single { OSMDataSource(get()) }

    single { SettingsRepository(get()) }

    viewModel { ShopListViewModel(get()) }

    viewModel { ProductsViewModel(get()) }

    viewModel { AddNewProductViewModel() }

    viewModel { (param1: Long) -> ProductsInShopListViewModel(param1, get()) }

    viewModel { AddShopListViewModel() }

    viewModel { SettingsViewModel(get()) }

}