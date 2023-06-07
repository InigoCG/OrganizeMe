package com.inigo.organizeme.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FirstPage
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.inigo.organizeme.codigo.AppBar
import com.inigo.organizeme.codigo.MenuItem
import com.inigo.organizeme.navegacion.AppPantallas
import com.inigo.organizeme.navegacion.DrawerBody
import com.inigo.organizeme.navegacion.DrawerHeader
import com.inigo.organizeme.ui.theme.OrganizeMeTheme
import kotlinx.coroutines.launch

@Composable
fun Info(navController: NavController) {
    OrganizeMeTheme {
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                AppBar(
                    nombre = "Info",
                    onNavigationIconClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }
                )
            },
            drawerContent = {
                DrawerHeader(nombre = "Info")
                DrawerBody(
                    items = listOf(
                        MenuItem(
                            id = "pagina_principal",
                            title = "Página principal",
                            description = "Ir a la página principal",
                            icon = Icons.Default.FirstPage
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "AGRADECIMIENTOS A:",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 25.dp)
                )
                val agradecimientos = listOf(
                    "A Ricardo por la ayuda brindada durante la elaboración del proyecto.",
                    "A mis compañeros de empresa; Sierro, Peña, Dani y Rafa por su apoyo y atención diarios.",
                    "A mi novia por la elaboración del logo de la aplicación y su apoyo incondicional en todo momento.",
                    "Y a mi familia por estar ahí siempre que lo he necesitado."
                )
                Spacer(modifier = Modifier.padding(top = 15.dp))
                Text(buildAnnotatedString {
                    agradecimientos.forEach {
                        withStyle(style = ParagraphStyle(textIndent = TextIndent(restLine = 12.sp))) {
                            append("\u2022")
                            append("\t\t")
                            append(it)
                            append("\n")
                        }
                    }
                })
            }
        }
    }
}