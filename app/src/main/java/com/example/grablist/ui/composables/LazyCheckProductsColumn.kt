package com.example.grablist.ui.composables

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.grablist.R
import com.example.grablist.data.database.Product
import com.example.grablist.ui.viewmodels.ProductsInListState
import com.example.grablist.ui.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyCheckProductsColumn(
    navController: NavController,
    state: ProductsInListState,
    modifier: Modifier,
    settingsViewModel: SettingsViewModel
) {
    var counter by remember { mutableIntStateOf(0) }
    val checked = remember { mutableStateListOf<Boolean>() }
    var showCompletedDialog by remember { mutableStateOf(false) }

    if (state.products.isNotEmpty() && checked.isEmpty()) {
        checked.addAll(List(state.products.size) { false })
        counter = state.products.size
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .padding(12.dp)
            .height(50.dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .weight(0.8F)
                    .height(10.dp),
                progress = { 1 - counter / state.products.size.toFloat() },
                color = MaterialTheme.colorScheme.tertiary,
                strokeCap = StrokeCap.Round,
            )
            Text(text = "${state.products.size-counter}/${state.products.size}",
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.2F),
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
                )
        }

        LazyColumn(modifier = Modifier) {
            items(state.products.size) {
                CheckProductItem(
                    state.products[it],
                    counter = {
                        if (checked[it]){
                            checked[it] = false
                            counter++
                        }else{
                            checked[it] = true
                            counter--
                            if (counter == 0){showCompletedDialog = true}
                        }
                              },
                    checked = checked[it])
            }
        }

        if (showCompletedDialog) {
            GenericAlertDialog(
                title = stringResource(R.string.completed_shopping_title),
                text = stringResource(R.string.completed_shopping_text),
                confirmText = stringResource(R.string.confirm),
                confirmAction = {
                    showCompletedDialog = false
                    settingsViewModel.increaseProgress()
                    settingsViewModel.setLatestProgressDate()
                    navController.navigateUp()
                },
                onDismissRequest = { },
                icon = Icons.Filled.CheckCircle,
            )
        }

    }

}

@Composable
fun CheckProductItem(product: Product, counter: () -> Unit, checked: Boolean){

    val colors: CardColors = when (checked) {
        true -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,)
        false -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary)
    }
    Card (
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 5.dp, vertical = 6.dp)
            .toggleable(
                value = checked,
                onValueChange = { counter() })
            .shadow(if (checked) 0.dp else 4.dp, CardDefaults.shape),
        shape = CardDefaults.shape,
        colors = colors
    ){
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconToggleButton(
                checked = checked,
                onCheckedChange = { counter() },
                colors = IconToggleButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary,
                    disabledContentColor = MaterialTheme.colorScheme.secondary,
                    checkedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    checkedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                when (checked) {
                    false -> Icon(Icons.Outlined.Circle, "empty")
                    true -> Icon(Icons.Outlined.CheckCircle, "checked")
                }
            }
            Column(
                modifier = Modifier.weight(0.60F)
            ) {
                if (checked){
                    Text(text = product.name, fontSize = 18.sp, textDecoration = TextDecoration.LineThrough, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }else{
                    Text(text = product.name, fontSize = 18.sp)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.25F),
                contentAlignment = Alignment.Center,
            ) {
                val imageUri = Uri.parse(product.imageUri)
                ImageWithPlaceholder(imageUri)
            }
        }
    }
}