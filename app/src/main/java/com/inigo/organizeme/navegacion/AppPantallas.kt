package com.inigo.organizeme.navegacion

sealed class AppPantallas(val ruta: String) {
    object Login: AppPantallas("login")
    object Registro: AppPantallas("registro")
    object PantallaSplash: AppPantallas("splash")
    object PantallaPrincipal: AppPantallas("pantalla_principal")
    object ListaDeTareas: AppPantallas("lista-tareas")
    object Info: AppPantallas("info")
    object Configuracion: AppPantallas("configuracion")
}
