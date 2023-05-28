package com.inigo.organizeme.pantallas

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.inigo.organizeme.codigo.ListaTareas
import com.inigo.organizeme.codigo.SharedViewModel
import com.inigo.organizeme.codigo.Tarea
import com.inigo.organizeme.codigo.Usuario
import com.inigo.organizeme.codigo.escribirDatosListaTareas
import com.inigo.organizeme.ui.theme.OrganizeMeTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ListaDeTareas(navController: NavController, sharedViewModel: SharedViewModel) {

    val context = LocalContext.current
    var usuario = sharedViewModel.usuario
    var listaTareas = sharedViewModel.listaTareas
    var index = sharedViewModel.index
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
                    text = "${listaTareas?.nombre}",
                    color = MaterialTheme.colors.onSecondary
                )
            }
        }, floatingActionButton = {
            FloatingActionButton(backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onSecondary,
                onClick = {
                    openDialog.value = true
                }) {
                if (openDialog.value) {
                    GestionarTarea(
                        null,
                        openDialog = openDialog,
                        context = context,
                        listaTareas!!,
                        usuario!!,
                        index!!
                    )
                }
                Icon(
                    imageVector = Icons.Default.Add, contentDescription = "Crear tarea"
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
                contenidoListaTareas(listaTareas, openDialog, context, usuario!!, index!!)
            }
        }
    }
}

@Composable
fun contenidoListaTareas(
    listaTareas: ListaTareas?,
    openDialog: MutableState<Boolean>,
    context: Context,
    usuario: Usuario,
    index: Int
) {
    LazyColumn(modifier = Modifier.padding(top = 10.dp)) {
        item {
            if (listaTareas != null) {
                if (listaTareas.tareas == null) {
                    listaTareas.tareas = mutableListOf()
                }
                if (!listaTareas.tareas?.isEmpty()!!) {
                    System.err.println("dentro del if")
                    for (tarea in listaTareas.tareas!!) {
                        listasDeTareas(tarea, openDialog, context, listaTareas, usuario, index)
                    }
                }
            }
        }
    }
}

@Composable
fun listasDeTareas(
    tarea: Tarea,
    openDialog: MutableState<Boolean>,
    context: Context,
    listaTareas: ListaTareas,
    usuario: Usuario,
    index: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                openDialog.value = true
            }, backgroundColor = MaterialTheme.colors.surface, elevation = 10.dp
    ) {
        if (openDialog.value) {
            GestionarTarea(
                tarea = tarea,
                openDialog = openDialog,
                context = context,
                listaTareas,
                usuario,
                index
            )
        }

        Row(modifier = Modifier.padding(10.dp)) {
            Text(
                fontSize = 20.sp,
                text = tarea.nombre.toString(),
                color = MaterialTheme.colors.onSecondary
            )
        }
    }
}

@Composable
fun GestionarTarea(
    tarea: Tarea?,
    openDialog: MutableState<Boolean>,
    context: Context,
    listaTareas: ListaTareas,
    usuario: Usuario,
    index: Int
) {
    var nombreTarea by remember { mutableStateOf(/*tarea.nombre*/ "") }
    var descripcionTarea by remember { mutableStateOf("") }

    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    val formatterDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("MMM dd yyyy").format(pickedDate)
        }
    }
    var pickedTime by remember {
        mutableStateOf(LocalTime.NOON)
    }
    val formatterTime by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("hh:mm").format(pickedTime)
        }
    }

    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    AlertDialog(onDismissRequest = { openDialog.value = false }, title = {
        Text(
            text = "Crear Tarea",
            color = MaterialTheme.colors.onBackground,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }, text = {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            TextField(
                value = nombreTarea,
                onValueChange = { nombreTarea = it },
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
                )
            )

            TextField(
                value = descripcionTarea,
                onValueChange = { descripcionTarea = it },
                label = {
                    Text(
                        text = "Descripción", color = MaterialTheme.colors.onBackground
                    )
                },
                modifier = Modifier.background(MaterialTheme.colors.onSecondary),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = MaterialTheme.colors.primaryVariant,
                    cursorColor = MaterialTheme.colors.primaryVariant,
                    textColor = MaterialTheme.colors.onBackground
                )
            )

            Button(onClick = {
                dateDialogState.show()
            }) {
                Text(text = "Selecciona una fecha")
            }

            Button(onClick = {
                timeDialogState.show()
            }) {
                Text(text = "Selecciona una hora")
            }
        }

        MaterialDialog(dialogState = dateDialogState, properties = DialogProperties(
            dismissOnBackPress = true, dismissOnClickOutside = true
        ), buttons = {
            positiveButton(text = "CONFIRMAR") {
                Toast.makeText(context, "fecha asignada", Toast.LENGTH_SHORT).show()
            }
            negativeButton(text = "CANCELAR")
        }) {
            datepicker(
                initialDate = LocalDate.now(),
                title = "FECHA",
            ) {
                pickedDate = it
            }
        }
        MaterialDialog(dialogState = timeDialogState, properties = DialogProperties(
            dismissOnBackPress = true, dismissOnClickOutside = true
        ), buttons = {
            positiveButton(text = "CONFIRMAR") {
                Toast.makeText(context, "Hora asignada", Toast.LENGTH_SHORT).show()
            }
            negativeButton(text = "CANCELAR")
        }) {
            timepicker(
                initialTime = LocalTime.NOON,
                title = "HORA",
            ) {
                pickedTime = it
            }
        }
    }, confirmButton = {
        Button(
            onClick = {
                if (comprobarNombreLista(nombreTarea)) {
                    openDialog.value = false
                    listaTareas.tareas?.add(Tarea(nombreTarea, descripcionTarea))
                    escribirDatosListaTareas(usuario, listaTareas, index)
                    // usuario?.listaTareas?.add(ListaTareas(nombreTarea))
                    // escribirDatosUsuario(usuario!!)
                    Toast.makeText(context, "Lista creada", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Campo vacío", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
        ) {
            Text(text = "CONFIRMAR", color = MaterialTheme.colors.onSecondary)
        }
    }, dismissButton = {
        Button(
            onClick = { openDialog.value = false },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
        ) {
            Text(text = "CANCELAR", color = MaterialTheme.colors.onSecondary)
        }
    }, backgroundColor = MaterialTheme.colors.onSecondary
    )
}