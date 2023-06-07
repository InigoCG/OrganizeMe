package com.inigo.organizeme.navegacion

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.inigo.organizeme.codigo.SharedViewModel
import com.inigo.organizeme.pantallas.*

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel()

    NavHost(navController = navController, startDestination = AppPantallas.PantallaSplash.ruta) {
        composable(route = AppPantallas.PantallaSplash.ruta) {
            PantallaSplash(navController)
        }
        composable(route = AppPantallas.Login.ruta) {
            ContenidoLogin(navController, sharedViewModel)
        }
        composable(route = AppPantallas.Registro.ruta) {
            ContenidoRegistro(navController)
        }
        composable(route = AppPantallas.PantallaPrincipal.ruta) {

            PantallaPrincipal(navController, sharedViewModel)
        }
        composable(route = AppPantallas.ListaDeTareas.ruta) {
            ListaDeTareas(navController, sharedViewModel)
        }
        composable(route = AppPantallas.Info.ruta) {
            Info(navController = navController)
        }
        composable(route = AppPantallas.Configuracion.ruta) {
            ConfiguracionListaTareas(navController, sharedViewModel)
        }
    }
}