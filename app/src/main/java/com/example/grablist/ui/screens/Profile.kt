package com.example.grablist.ui.screens

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.grablist.R
import com.example.grablist.data.database.Tier
import com.example.grablist.data.database.evaluateTier
import com.example.grablist.ui.composables.MainBottomAppBar
import com.example.grablist.ui.composables.MainTopAppBar
import com.example.grablist.ui.composables.TierProgressBar
import com.example.grablist.ui.viewmodels.ProductState
import com.example.grablist.ui.viewmodels.ProductsViewModel
import com.example.grablist.ui.viewmodels.SettingsState
import com.example.grablist.ui.viewmodels.ShopListState

@Composable
fun Profile(navController: NavController,
            settingsState: SettingsState,
            shopListState: ShopListState,
            productVm: ProductsViewModel){

    val productsFavorites by productVm.favorites.collectAsStateWithLifecycle()
    val tier: Tier = evaluateTier(settingsState.progress)

    Scaffold(
        topBar = { MainTopAppBar(
            navController = navController,
            title = stringResource(id = R.string.prof_title),
            goBack = false) },
        bottomBar = { MainBottomAppBar(
            navController = navController,
            active = 3
        ) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box(
                    modifier = Modifier.padding(12.dp).size(250.dp, 250.dp),
                    contentAlignment = Alignment.TopEnd,
                    ) {
                    Image(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "ProfileImage",
                    modifier = Modifier.fillMaxSize(),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground))

                    if (tier != Tier.None){
                        Image(
                            painter = painterResource(tier.iconId),
                            contentDescription = "tierImage",
                            modifier = Modifier.padding(15.dp).size(65.dp))
                    }

                }
            }
            item {
                Text(
                    text = settingsState.username,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
                    fontSize = 24.sp
                )
            }
            item { ProfileEntry(stringResource(R.string.home_title), shopListState.shopLists.size) }
            item { ProfileEntry(stringResource(R.string.favs_title), productsFavorites.products.size) }
            item { TierProgressBar(settingsState.progress) }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "information",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(0.15F)
                    )
                    Text(
                        text = stringResource(R.string.progress_info),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(0.85F)
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(50.dp)) }
        }
    }
}



@Composable
fun ProfileEntry(
    text: String,
    value: Int?
){
    HorizontalDivider(thickness = 2.dp)
    Text(
        text = "${text}: $value",
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp),
        fontSize = 20.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}