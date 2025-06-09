package com.example.grablist.ui.composables

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grablist.R
import com.example.grablist.data.database.Product
import com.example.grablist.data.database.ShopList
import com.example.grablist.ui.viewmodels.ProductsViewModel

@Composable
fun LazyProductColumn(
    showFavorites: Boolean = false,
    products: List<Product>,
    shopList: ShopList?,
    vm: ProductsViewModel,
    modifier: Modifier = Modifier,
    onClick: (product: Product) -> Unit = {},
    noInteractions: Boolean = false,
    canClick: (product: Product) -> Boolean = { true }
) {

    LazyColumn(modifier = modifier.fillMaxSize()) {
        if (products.isNotEmpty()){
            items(products) {item ->
                if (canClick(item)){
                    ProductCard(
                        product = item,
                        vm = vm,
                        shopList = shopList,
                        showFavorites = showFavorites,
                        onClick = onClick,
                        noInteractions = noInteractions)
                }
            }
        }
        item { Spacer(modifier = Modifier.height(75.dp)) }
    }

}

@Composable
fun ProductCard(
    product: Product,
    vm: ProductsViewModel,
    shopList: ShopList?,
    showFavorites: Boolean,
    onClick: (product: Product) -> Unit,
    noInteractions: Boolean
){
    var expanded by remember { mutableStateOf(false) }
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 5.dp, vertical = 6.dp)
            .clickable(
                onClick = { onClick(product) }
            ),
        shape = CardDefaults.shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.25F),
                contentAlignment = Alignment.BottomEnd,
            ) {
                val imageUri = Uri.parse(product.imageUri)
                ImageWithPlaceholder(imageUri)
                if (product.favorite && showFavorites){
                    Icon(Icons.Filled.Favorite, "favorite",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(3.dp)
                            .background(MaterialTheme.colorScheme.secondary, shape = CircleShape)
                            .padding(5.dp)
                            .size(15.dp))
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .weight(0.65F)
            ) {
                Text(
                    text = product.name,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(5.dp)
                        .weight(0.75F),
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }

            Box(modifier = Modifier.weight(0.1F)) {
                if (!noInteractions){
                    if (shopList != null){
                        IconButton(
                            onClick = { expanded = !expanded },
                            modifier = Modifier
                                .padding(horizontal = 3.dp, vertical = 12.dp),
                            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        ) {
                            Icon(Icons.Filled.MoreVert, "More")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.remove_generic)) },
                                onClick = {
                                    vm.deleteProduct(product, shopList)
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Favorite") },
                                onClick = {
                                    vm.changeFavorite(product)
                                    expanded = false
                                }
                            )
                        }
                    }
                    else{
                        IconToggleButton(
                            modifier = Modifier
                                .padding(horizontal = 3.dp, vertical = 12.dp),
                            colors = IconToggleButtonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                disabledContainerColor = MaterialTheme.colorScheme.primary,
                                disabledContentColor = MaterialTheme.colorScheme.onSecondary,
                                checkedContainerColor = MaterialTheme.colorScheme.secondary,
                                checkedContentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            checked = product.favorite,
                            onCheckedChange = { vm.changeFavorite(product) },
                        ) {
                            Icon(Icons.Filled.Favorite, "fav")
                        }
                    }
                }
            }
        }
    }
}
