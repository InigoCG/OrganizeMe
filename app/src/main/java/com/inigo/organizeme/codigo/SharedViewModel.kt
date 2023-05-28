package com.inigo.organizeme.codigo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    var usuario by mutableStateOf<Usuario?>(null)
        private set

    var listaTareas by mutableStateOf<ListaTareas?>(null)
        private set

    var index: Int? = null

    fun addUsuario(nuevoUsuario: Usuario) {
        usuario = nuevoUsuario
    }

    fun addListaTareas(nuevaListaTareas: ListaTareas) {
        listaTareas = nuevaListaTareas
    }

    fun addIndex(index: Int?) {
        this.index = index
    }
}