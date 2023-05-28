package com.inigo.organizeme.pantallas

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.inigo.organizeme.codigo.*
import com.inigo.organizeme.navegacion.AppPantallas
import com.inigo.organizeme.ui.theme.OrganizeMeTheme

@Composable
fun PantallaPrincipal(navController: NavController, sharedViewModel: SharedViewModel) {

    val context = LocalContext.current
    val usuario = sharedViewModel.usuario
    val openDialog = remember { mutableStateOf(false) }

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
                onClick = {
                    openDialog.value = true
                }) {
                if (openDialog.value) {
                    crearLista(openDialog, usuario, context)
                }
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear lista de tareas"
                )
            }
        }, floatingActionButtonPosition = FabPosition.End) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                var estado by remember { mutableStateOf(usuario?.listaTareas) }
                contenido(usuario, sharedViewModel, navController)
            }
        }
    }
}

@Composable
fun contenido(usuario: Usuario?, sharedViewModel: SharedViewModel, navController: NavController) {
    LazyColumn(modifier = Modifier.padding(top = 10.dp)) {
        item {
            if (usuario != null) {
                if (usuario.listaTareas == null) {
                    usuario.listaTareas = mutableListOf()
                }
                if (!usuario.listaTareas?.isEmpty()!!) {
                    usuario.listaTareas!!.forEachIndexed { index, lista ->
                        listas(lista, sharedViewModel, navController, usuario, index)
                    }
                    /*for (lista in usuario.listaTareas!!) {
                        listas(lista, sharedViewModel, navController, usuario)
                    }*/
                }
            }
        }
    }
}

@Composable
fun listas(
    lista: ListaTareas,
    sharedViewModel: SharedViewModel,
    navController: NavController,
    usuario: Usuario?,
    index: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                sharedViewModel.addUsuario(usuario!!)
                sharedViewModel.addListaTareas(lista)
                sharedViewModel.addIndex(index)
                navController.navigate(AppPantallas.ListaDeTareas.ruta)
            },
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 10.dp
    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            Text(
                fontSize = 20.sp,
                text = lista.nombre.toString(),
                color = MaterialTheme.colors.onSecondary
            )
        }
    }
}

@Composable
fun crearLista(openDialog: MutableState<Boolean>, usuario: Usuario?, context: Context) {
    var nombreLista by remember { mutableStateOf("") }
    var estadoLista by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { openDialog.value = false },
        title = {
            Text(
                text = "Crear lista",
                color = MaterialTheme.colors.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            TextField(
                value = nombreLista,
                onValueChange = { nombreLista = it },
                label = {
                    Text(
                        text = "Nombre", color = MaterialTheme.colors.onBackground
                    )
                },
                modifier = Modifier
                    .background(MaterialTheme.colors.onSecondary),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = MaterialTheme.colors.primaryVariant,
                    cursorColor = MaterialTheme.colors.primaryVariant,
                    textColor = MaterialTheme.colors.onBackground
                )
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (comprobarNombreLista(nombreLista)) {
                        openDialog.value = false
                        usuario?.listaTareas?.add(ListaTareas(nombreLista))
                        escribirDatosUsuario(usuario!!)
                        estadoLista = true
                        Toast.makeText(context, "Lista creada", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Campo vacío", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
            ) {
                Text(text = "CONFIRMAR", color = MaterialTheme.colors.onSecondary)
                if (estadoLista) {

                }
                estadoLista = false
            }
        },
        dismissButton = {
            Button(
                onClick = { openDialog.value = false },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
            ) {
                Text(text = "CANCELAR", color = MaterialTheme.colors.onSecondary)
            }
        },
        backgroundColor = MaterialTheme.colors.onSecondary
    )
}

fun comprobarNombreLista(nombreLista: String): Boolean {
    var resultado = false

    if (nombreLista.isNotBlank()) {
        resultado = true
    }
    return resultado
}