package com.inigo.organizeme.pantallas

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.inigo.organizeme.codigo.SharedViewModel
import com.inigo.organizeme.ui.theme.OrganizeMeTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ListaDeTareas(navController: NavController, sharedViewModel: SharedViewModel) {

    val usuario = sharedViewModel.usuario

    OrganizeMeTheme {
        Scaffold(topBar = {
            TopAppBar(backgroundColor = MaterialTheme.colors.primaryVariant) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = "Menu",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colors.onSecondary
                )
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    fontSize = 20.sp,
                    text = "${usuario?.nombre}",
                    color = MaterialTheme.colors.onSecondary
                )
            }
        }, floatingActionButton = {
            FloatingActionButton(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onSecondary,
                onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear tarea"
                )
            }
        }, floatingActionButtonPosition = FabPosition.End) {

        }
    }
}