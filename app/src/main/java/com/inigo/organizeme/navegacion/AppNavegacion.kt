package com.inigo.organizeme.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inigo.organizeme.pantallas.ContenidoLogin
import com.inigo.organizeme.pantallas.ContenidoRegistro
import com.inigo.organizeme.pantallas.PantallaPrincipal
import com.inigo.organizeme.pantallas.PantallaSplash

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppPantallas.PantallaSplash.ruta) {
        composable(route = AppPantallas.PantallaSplash.ruta) {
            PantallaSplash(navController)
        }
        composable(route = AppPantallas.Login.ruta) {
            ContenidoLogin(navController)
        }
        composable(route = AppPantallas.Registro.ruta) {
            ContenidoRegistro(navController)
        }
        composable(route = AppPantallas.PantallaPrincipal.ruta) {
            PantallaPrincipal(navController)
        }
    }
}