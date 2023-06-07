package com.inigo.organizeme.pantallas

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FirstPage
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inigo.organizeme.codigo.AppBar
import com.inigo.organizeme.codigo.ListaTareas
import com.inigo.organizeme.codigo.MenuItem
import com.inigo.organizeme.codigo.SharedViewModel
import com.inigo.organizeme.codigo.actualizarDatosUsuario
import com.inigo.organizeme.codigo.eliminarDatosUsuario
import com.inigo.organizeme.codigo.eliminarTarea
import com.inigo.organizeme.navegacion.AppPantallas
import com.inigo.organizeme.navegacion.DrawerBody
import com.inigo.organizeme.navegacion.DrawerHeader
import com.inigo.organizeme.ui.theme.OrganizeMeTheme
import kotlinx.coroutines.launch

@Composable
fun ConfiguracionListaTareas(navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    var usuario = sharedViewModel.usuario
    var listaTareas = sharedViewModel.listaTareas
    var index = sharedViewModel.index

    OrganizeMeTheme {
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                AppBar(
                    nombre = listaTareas?.nombre.toString(),
                    onNavigationIconClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }
                )
            },
            drawerContent = {
                DrawerHeader(nombre = listaTareas?.nombre.toString())
                DrawerBody(
                    items = listOf(
                        MenuItem(
                            id = "pagina_principal",
                            title = "Página principal",
                            description = "Ir a la página principal",
                            icon = Icons.Default.FirstPage
                        ),
                        MenuItem(
                            id = "info",
                            title = "Info",
                            description = "Ir a información",
                            icon = Icons.Default.Info
                        ),
                        MenuItem(
                            id = "cerrar_sesion",
                            title = "Cerrar Sesión",
                            description = "Cierra la sesión del usuario",
                            icon = Icons.Default.Logout
                        )
                    ),
                    onItemClick = {
                        when (it.id) {
                            "pagina_principal" -> {
                                navController.popBackStack()
                                navController.navigate(AppPantallas.PantallaPrincipal.ruta)
                            }

                            "cerrar_sesion" -> {
                                navController.popBackStack()
                                navController.navigate(AppPantallas.Login.ruta)
                            }

                            "info" -> {
                                navController.navigate(AppPantallas.Info.ruta)
                            }
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                var nombreNuevo by remember { mutableStateOf(listaTareas?.nombre.toString()) }
                val focusManager = LocalFocusManager.current

                TextField(
                    value = nombreNuevo,
                    onValueChange = { nombreNuevo = it },
                    label = {
                        Text(
                            text = "Nombre", color = MaterialTheme.colors.onBackground
                        )
                    },
                    modifier = Modifier.background(MaterialTheme.colors.onSecondary),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = MaterialTheme.colors.primaryVariant,
                        cursorColor = MaterialTheme.colors.primaryVariant,
                        textColor = MaterialTheme.colors.onBackground
                    ),
                    singleLine = true,
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                    })
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            var listaTareasNueva = ListaTareas(nombreNuevo, listaTareas?.tareas)

                            usuario?.listaTareas?.set(index!!, listaTareasNueva)
                            actualizarDatosUsuario(usuario!!)
                            Toast.makeText(context, "Lista Actualizada", Toast.LENGTH_SHORT).show()

                            navController.popBackStack()
                            sharedViewModel.addUsuario(usuario)
                            sharedViewModel.addListaTareas(listaTareasNueva)
                            navController.navigate(AppPantallas.ListaDeTareas.ruta)
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                    ) {
                        Text(text = "ACTUALIZAR", color = MaterialTheme.colors.onSecondary)
                    }
                    Button(
                        onClick = {
                            usuario?.listaTareas?.remove(listaTareas)
                            eliminarDatosUsuario(usuario!!, index!!)
                            Toast.makeText(context, "Lista eliminada", Toast.LENGTH_SHORT).show()

                            navController.popBackStack()
                            sharedViewModel.addUsuario(usuario)
                            navController.navigate(AppPantallas.PantallaPrincipal.ruta)
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                    ) {
                        Text(text = "ELMINAR", color = MaterialTheme.colors.onSecondary)
                    }
                }
            }
        }
    }
}