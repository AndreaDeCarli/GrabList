package com.example.grablist.ui.screens

import android.graphics.drawable.Icon
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.grablist.R
import com.example.grablist.data.database.Product
import com.example.grablist.ui.composables.ImageWithPlaceholder
import com.example.grablist.ui.composables.MainTopAppBar

@Composable
fun ProductDetailsScreen(navController: NavController, product: Product){
    Scaffold(
        topBar = { MainTopAppBar(navController = navController, title = product.name, goBack = true) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            Box(
                modifier = Modifier.size(360.dp, 360.dp)
            ){
                ImageWithPlaceholder(Uri.parse(product.imageUri))
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp).fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.name_generic) + ":")
                Text(text = product.name)
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp).fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.favs_title) + ":")
                when {
                    product.favorite -> Icon(Icons.Filled.Favorite, "fav")
                    else -> Icon(Icons.Outlined.FavoriteBorder, "fav")
                }
            }
        }
    }
}