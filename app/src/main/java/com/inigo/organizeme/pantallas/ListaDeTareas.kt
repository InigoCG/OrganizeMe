package com.inigo.organizeme.pantallas

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FirstPage
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.inigo.organizeme.codigo.AlarmItem
import com.inigo.organizeme.codigo.AndroidAlarmScheduler
import com.inigo.organizeme.codigo.AppBar
import com.inigo.organizeme.codigo.ListaTareas
import com.inigo.organizeme.codigo.MenuItem
import com.inigo.organizeme.codigo.SharedViewModel
import com.inigo.organizeme.codigo.Tarea
import com.inigo.organizeme.codigo.Usuario
import com.inigo.organizeme.codigo.eliminarTarea
import com.inigo.organizeme.codigo.escribirDatosListaTareas
import com.inigo.organizeme.navegacion.AppPantallas
import com.inigo.organizeme.navegacion.DrawerBody
import com.inigo.organizeme.navegacion.DrawerHeader
import com.inigo.organizeme.ui.theme.OrganizeMeTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Formatter

@Composable
fun ListaDeTareas(
    navController: NavController,
    sharedViewModel: SharedViewModel,
) {

    val context = LocalContext.current
    var usuario = sharedViewModel.usuario
    var listaTareas = sharedViewModel.listaTareas
    var index = sharedViewModel.index
    val openDialog1 = remember { mutableStateOf(false) }
    val openDialog2 = remember { mutableStateOf(false) }

    /*var hasNotificationPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else mutableStateOf(true)
    }*/

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
                            id = "configuracion",
                            title = "Configuración",
                            description = "Ir a configuración",
                            icon = Icons.Default.Settings
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

                            "configuracion" -> {
                                sharedViewModel.addListaTareas(listaTareas!!)
                                sharedViewModel.addUsuario(usuario!!)
                                sharedViewModel.addIndex(index)
                                navController.navigate(AppPantallas.Configuracion.ruta)
                            }
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onSecondary,
                    onClick = {
                        openDialog1.value = true
                    }) {
                    if (openDialog1.value) {
                        TareaAdd(
                            openDialog = openDialog1,
                            context = context,
                            listaTareas!!,
                            usuario!!,
                            index!!,
                            navController
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Add, contentDescription = "Crear tarea"
                    )
                }
            }, floatingActionButtonPosition = FabPosition.End
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                contenidoListaTareas(
                    listaTareas,
                    openDialog2,
                    context,
                    usuario!!,
                    index!!,
                    navController
                )
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
    index: Int,
    navController: NavController,
) {
    LazyColumn(modifier = Modifier.padding(top = 10.dp)) {
        item {
            if (listaTareas != null) {
                if (listaTareas.tareas == null) {
                    listaTareas.tareas = mutableListOf()
                }
                if (!listaTareas.tareas?.isEmpty()!!) {
                    listaTareas.tareas!!.forEachIndexed { indexTarea, tarea ->
                        listasDeTareas(
                            tarea,
                            openDialog,
                            context,
                            listaTareas,
                            usuario,
                            index,
                            indexTarea,
                            navController
                        )
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
    index: Int,
    indexTarea: Int,
    navController: NavController
) {
    var aux = -1
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                openDialog.value = true
                aux = indexTarea
            }, backgroundColor = MaterialTheme.colors.surface, elevation = 10.dp
    ) {
        if (openDialog.value && aux == indexTarea) {
            GestionarTarea(
                openDialog = openDialog,
                context = context,
                listaTareas,
                usuario,
                index,
                indexTarea,
                tarea,
                navController
            )
            aux = -1
        }

        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                fontSize = 20.sp,
                text = tarea.nombre.toString(),
                color = MaterialTheme.colors.onSecondary
            )
            Text(
                fontSize = 15.sp,
                text = tarea.fecha.toString(),
                color = MaterialTheme.colors.onSecondary
            )
        }
    }
}

@Composable
fun TareaAdd(
    openDialog: MutableState<Boolean>,
    context: Context,
    listaTareas: ListaTareas,
    usuario: Usuario,
    index: Int,
    navController: NavController,
) {
    var nombreTarea by remember { mutableStateOf(/*tarea.nombre*/ "") }
    var descripcionTarea by remember { mutableStateOf("") }

    var fechaElegida by remember { mutableStateOf(false) }
    var horaElegida by remember { mutableStateOf(false) }

    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    val formatterDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("yyyy-MM-dd").format(pickedDate)
        }
    }
    var pickedTime by remember {
        mutableStateOf(LocalTime.NOON)
    }
    val formatterTime by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("HH:mm").format(pickedTime)
        }
    }

    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    val focusManager = LocalFocusManager.current

    var alarmItem: AlarmItem? = null

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
                ),
                singleLine = true,
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                })
            )
            Spacer(modifier = Modifier.padding(bottom = 5.dp))
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
                ),
                singleLine = true,
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                })
            )
            Spacer(modifier = Modifier.padding(bottom = 15.dp))
            Button(onClick = {
                dateDialogState.show()
            }) {
                Text(text = "Selecciona una fecha", color = MaterialTheme.colors.onSecondary)
            }
            Spacer(modifier = Modifier.padding(bottom = 10.dp))
            Button(onClick = {
                timeDialogState.show()
            }) {
                Text(text = "Selecciona una hora", color = MaterialTheme.colors.onSecondary)
            }
        }

        MaterialDialog(dialogState = dateDialogState, properties = DialogProperties(
            dismissOnBackPress = true, dismissOnClickOutside = true
        ), backgroundColor = MaterialTheme.colors.onSecondary, buttons = {
            positiveButton(text = "CONFIRMAR") {
                fechaElegida = true
                Toast.makeText(context, "fecha asignada", Toast.LENGTH_SHORT).show()
            }
            negativeButton(text = "CANCELAR")
        }) {
            datepicker(
                initialDate = LocalDate.now(),
                title = "FECHA",
                colors = DatePickerDefaults.colors(
                    headerBackgroundColor = MaterialTheme.colors.primaryVariant,
                    headerTextColor = MaterialTheme.colors.onSecondary,
                    dateActiveBackgroundColor = MaterialTheme.colors.primary,
                    dateActiveTextColor = MaterialTheme.colors.onSecondary,
                    dateInactiveTextColor = MaterialTheme.colors.onBackground,
                    calendarHeaderTextColor = MaterialTheme.colors.primary
                )
            ) {
                pickedDate = it
            }
        }
        MaterialDialog(dialogState = timeDialogState, properties = DialogProperties(
            dismissOnBackPress = true, dismissOnClickOutside = true
        ), backgroundColor = MaterialTheme.colors.onSecondary, buttons = {
            positiveButton(text = "CONFIRMAR") {
                horaElegida = true
                Toast.makeText(context, "Hora asignada", Toast.LENGTH_SHORT).show()
            }
            negativeButton(text = "CANCELAR")
        }) {
            timepicker(
                initialTime = LocalTime.NOON,
                title = "HORA",
                colors = TimePickerDefaults.colors(
                    activeTextColor = MaterialTheme.colors.onSecondary,
                    activeBackgroundColor = MaterialTheme.colors.primary,
                    inactiveBackgroundColor = MaterialTheme.colors.primaryVariant,
                    inactiveTextColor = MaterialTheme.colors.onSecondary,
                    selectorTextColor = MaterialTheme.colors.onSecondary,
                    inactivePeriodBackground = MaterialTheme.colors.secondary,
                    selectorColor = MaterialTheme.colors.primary
                ),
                is24HourClock = true
            ) {
                pickedTime = it
            }
        }
    }, confirmButton = {
        Button(
            onClick = {
                if (comprobarNombreLista(nombreTarea) && fechaElegida && horaElegida) {
                    openDialog.value = false
                    fechaElegida = false
                    horaElegida = false

                    val fecha: LocalDate = LocalDate.parse(formatterDate)
                    val hora: LocalTime = LocalTime.parse(formatterTime)
                    val tiempo: LocalDateTime = LocalDateTime.of(fecha, hora)
                    val formatter: DateTimeFormatter =
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    val tiempoFormateado: String = tiempo.format(formatter)

                    val tarea = Tarea(nombreTarea, descripcionTarea, tiempoFormateado)
                    listaTareas.tareas?.add(tarea)

                    escribirDatosListaTareas(usuario, listaTareas, index)

                    val scheduler = AndroidAlarmScheduler(context, tiempo)

                    alarmItem = AlarmItem(
                        fecha = tiempo,
                        titulo = tarea.nombre!!,
                        mensaje = tiempoFormateado
                    )
                    alarmItem?.let(scheduler::schedule)

                    Toast.makeText(context, "Tarea creada", Toast.LENGTH_SHORT).show()

                    navController.popBackStack()
                    navController.navigate(AppPantallas.ListaDeTareas.ruta)
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

@Composable
fun GestionarTarea(
    openDialog: MutableState<Boolean>,
    context: Context,
    listaTareas: ListaTareas,
    usuario: Usuario,
    indexLista: Int,
    indexTarea: Int,
    tarea: Tarea,
    navController: NavController
) {
    val localDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val fechaInicial: LocalDateTime = LocalDateTime.parse(tarea.fecha, localDateTimeFormatter)

    var nombreTarea by remember { mutableStateOf(tarea.nombre) }
    var descripcionTarea by remember { mutableStateOf(tarea.descripcion) }

    var fechaElegida by remember { mutableStateOf(false) }
    var horaElegida by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    var pickedDate by remember {
        mutableStateOf(fechaInicial.toLocalDate())
    }
    val formatterDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("yyyy-MM-dd").format(pickedDate)
        }
    }
    var pickedTime by remember {
        mutableStateOf(fechaInicial.toLocalTime())
    }
    val formatterTime by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("HH:mm").format(pickedTime)
        }
    }

    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    var alarmItem: AlarmItem? = null

    AlertDialog(
        onDismissRequest = { openDialog.value = false }, title = {
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
                    value = nombreTarea!!,
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
                    ),
                    singleLine = true,
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                    })
                )
                Spacer(modifier = Modifier.padding(bottom = 5.dp))
                TextField(
                    value = descripcionTarea!!,
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
                    ),
                    singleLine = true,
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                    })
                )
                Spacer(modifier = Modifier.padding(bottom = 15.dp))
                Button(onClick = {
                    dateDialogState.show()
                }) {
                    Text(text = "Selecciona una fecha", color = MaterialTheme.colors.onSecondary)
                }
                Text(text = formatterDate, color = MaterialTheme.colors.onBackground)
                Spacer(modifier = Modifier.padding(bottom = 10.dp))
                Button(onClick = {
                    timeDialogState.show()
                }) {
                    Text(text = "Selecciona una hora", color = MaterialTheme.colors.onSecondary)
                }
                Text(text = formatterTime, color = MaterialTheme.colors.onBackground)
            }

            MaterialDialog(dialogState = dateDialogState, properties = DialogProperties(
                dismissOnBackPress = true, dismissOnClickOutside = true
            ), backgroundColor = MaterialTheme.colors.onSecondary, buttons = {
                positiveButton(text = "CONFIRMAR") {
                    fechaElegida = true
                    Toast.makeText(context, "fecha asignada", Toast.LENGTH_SHORT).show()
                }
                negativeButton(text = "CANCELAR")
            }) {
                datepicker(
                    initialDate = LocalDate.now(),
                    title = "FECHA",
                    colors = DatePickerDefaults.colors(
                        headerBackgroundColor = MaterialTheme.colors.primaryVariant,
                        headerTextColor = MaterialTheme.colors.onSecondary,
                        dateActiveBackgroundColor = MaterialTheme.colors.primary,
                        dateActiveTextColor = MaterialTheme.colors.onSecondary,
                        dateInactiveTextColor = MaterialTheme.colors.onBackground,
                        calendarHeaderTextColor = MaterialTheme.colors.primary
                    )
                ) {
                    pickedDate = it
                }
            }
            MaterialDialog(dialogState = timeDialogState, properties = DialogProperties(
                dismissOnBackPress = true, dismissOnClickOutside = true
            ), backgroundColor = MaterialTheme.colors.onSecondary, buttons = {
                positiveButton(text = "CONFIRMAR") {
                    horaElegida = true
                    Toast.makeText(context, "Hora asignada", Toast.LENGTH_SHORT).show()
                }
                negativeButton(text = "CANCELAR")
            }) {
                timepicker(
                    initialTime = LocalTime.NOON,
                    title = "HORA",
                    colors = TimePickerDefaults.colors(
                        activeTextColor = MaterialTheme.colors.onSecondary,
                        activeBackgroundColor = MaterialTheme.colors.primary,
                        inactiveBackgroundColor = MaterialTheme.colors.primaryVariant,
                        inactiveTextColor = MaterialTheme.colors.onSecondary,
                        selectorTextColor = MaterialTheme.colors.onSecondary,
                        inactivePeriodBackground = MaterialTheme.colors.secondary,
                        selectorColor = MaterialTheme.colors.primary
                    ),
                    is24HourClock = true
                ) {
                    pickedTime = it
                }
            }
        }, confirmButton = {
            Button(
                onClick = {
                    if (comprobarNombreLista(nombreTarea!!) && fechaElegida && horaElegida) {
                        openDialog.value = false
                        fechaElegida = false
                        horaElegida = false

                        val fecha: LocalDate = LocalDate.parse(formatterDate)
                        val hora: LocalTime = LocalTime.parse(formatterTime)
                        val tiempo: LocalDateTime = LocalDateTime.of(fecha, hora)
                        val formatter: DateTimeFormatter =
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                        val tiempoFormateado: String = tiempo.format(formatter)

                        listaTareas.tareas?.set(
                            indexTarea, Tarea(nombreTarea, descripcionTarea, tiempoFormateado)
                        )
                        escribirDatosListaTareas(usuario, listaTareas, indexLista)

                        val scheduler = AndroidAlarmScheduler(context, tiempo)

                        alarmItem = AlarmItem(
                            fecha = tiempo,
                            titulo = tarea.nombre!!,
                            mensaje = tiempoFormateado
                        )
                        alarmItem?.let(scheduler::schedule)

                        Toast.makeText(context, "Tarea editada", Toast.LENGTH_SHORT).show()

                        navController.popBackStack()
                        navController.navigate(AppPantallas.ListaDeTareas.ruta)
                    } else {
                        Toast.makeText(context, "Campo vacío", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
            ) {
                Text(text = "CONFIRMAR", color = MaterialTheme.colors.onSecondary)
            }
            Button(
                onClick = {
                    openDialog.value = false

                    listaTareas.tareas?.remove(tarea)
                    eliminarTarea(usuario, listaTareas, indexLista, indexTarea)

                    Toast.makeText(context, "Tarea eliminada", Toast.LENGTH_SHORT).show()

                    navController.popBackStack()
                    navController.navigate(AppPantallas.ListaDeTareas.ruta)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
            ) {
                Text(text = "ELMINAR", color = MaterialTheme.colors.onSecondary)
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