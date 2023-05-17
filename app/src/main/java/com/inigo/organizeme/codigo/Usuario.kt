package com.inigo.organizeme.codigo

class Usuario(
    val email: String? = "",
    val nombre: String? = "",
    var listaTareas: MutableList<ListaTareas>? = mutableListOf(
        ListaTareas("Tareas predeterminadas 1"),
        ListaTareas("Tareas predeterminadas 2"),
        ListaTareas("Tareas predeterminadas 3"),
        )
)

