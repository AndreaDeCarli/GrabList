package com.example.grablist.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.grablist.R
import com.example.grablist.ui.composables.MainTopAppBar
import com.example.grablist.ui.viewmodels.AddShopListActions
import com.example.grablist.ui.viewmodels.AddShopListState

@Composable
fun AddNewList (state: AddShopListState, actions: AddShopListActions, onSubmit: () -> Unit, navController: NavController){
    Scaffold (
        topBar = { MainTopAppBar(navController, stringResource(id = R.string.new_list_title), true) },
        floatingActionButton = { FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.tertiary,
            onClick = {
                if (!state.canSubmit) return@FloatingActionButton
                onSubmit()
                navController.navigateUp()
            },
            shape = CircleShape
        ) {
            Icon(Icons.Outlined.Check, "Add")
        } }
    ) { innerPadding ->
        Column (
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            OutlinedTextField(
                onValueChange = actions::setTitle,
                value = state.title,
                label = { Text(stringResource(id = R.string.title_generic)) },
                modifier = Modifier.padding(12.dp).fillMaxWidth())

            OutlinedTextField(
                onValueChange = actions::setDate,
                value = state.date,
                label = { Text(stringResource(id = R.string.date_generic)) },
                modifier = Modifier.padding(12.dp).fillMaxWidth())

            var selected by remember { mutableIntStateOf(0) }
            val imagesIds = listOf(R.drawable.sprite_0, R.drawable.sprite_1,R.drawable.sprite_2,R.drawable.sprite_3,R.drawable.sprite_4, R.drawable.sprite_5,R.drawable.sprite_6,R.drawable.sprite_7)
            actions.setIcon(imagesIds[selected].toLong())
            LazyRow() {
                items(imagesIds.size){
                    if (it == selected){
                        Image(
                            painter = painterResource(imagesIds[it]),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(90.dp)
                                .selectable(selected = selected == it, onClick = { selected = it;actions.setIcon(imagesIds[it].toLong()) })
                                .border(3.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(20.dp))
                                .clip(RoundedCornerShape(20.dp))
                                .background(MaterialTheme.colorScheme.tertiary)
                            ,
                            contentDescription = "deca",
                        )
                    }else{
                        Image(
                            painter = painterResource(imagesIds[it]),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(90.dp)
                                .selectable(selected = selected == it, onClick = {selected = it;actions.setIcon(imagesIds[it].toLong())})
                                .clip(RoundedCornerShape(20.dp)),
                            contentDescription = "deca")
                    }
                     }
                }
        }
    }
}