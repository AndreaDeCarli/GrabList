package com.example.grablist.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.grablist.R
import com.example.grablist.data.database.ShopList
import com.example.grablist.ui.NavRoute
import com.example.grablist.ui.viewmodels.ShopListState
import com.example.grablist.ui.viewmodels.ShopListViewModel


@Composable
fun LazyShopListColumn(
    navController: NavController,
    state: ShopListState,
    vm: ShopListViewModel,

){
    if (state.shopLists.isNotEmpty()){
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.shopLists) {item ->
                ShoppingListCard(
                    item = item,
                    vm = vm,
                    onClick = { navController.navigate(NavRoute.ShopListDetails(item.shopListId)) })
            }
            item { Spacer(modifier = Modifier.height(75.dp)) }
        }
    }

}

@Composable
fun ShoppingListCard(item: ShopList, vm: ShopListViewModel, onClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }
    Card(
        onClick = onClick,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 5.dp, vertical = 6.dp),
        shape = CardDefaults.shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.25F),
                contentAlignment = Alignment.Center,
            ){
                Image(painterResource(item.iconId.toInt()),
                    contentDescription = "List picture",
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .fillMaxSize()
                        .padding(5.dp)
                )
            }
            Column (
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .weight(0.65F)
            ) {
                Text(text = item.title, fontSize = 20.sp, modifier = Modifier.padding(5.dp), color = MaterialTheme.colorScheme.onSecondary)
                Text(
                    text = item.date,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(5.dp),
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }

            Box(modifier = Modifier.weight(0.1F)){
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
                            showAlert = true
                            expanded = false
                        }
                    )
                }
            }
            if (showAlert){
                GenericAlertDialog(
                    title = stringResource(R.string.delete_shoplist_dialog_title),
                    text = stringResource(R.string.delete_shoplist_dialog_text),
                    confirmText = stringResource(R.string.confirm),
                    confirmAction = { vm.deleteShopList(item)
                                    showAlert = false},
                    dismissText = stringResource(R.string.dismiss),
                    dismissAction = { showAlert = false },
                    onDismissRequest = { showAlert = false },
                    icon = Icons.Outlined.Delete
                )
            }
        }
    }
}