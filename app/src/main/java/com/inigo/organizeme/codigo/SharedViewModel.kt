package com.inigo.organizeme.codigo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {

    var usuario by mutableStateOf<Usuario?>(null)
        private set

    fun addUsuario(nuevoUsuario: Usuario) {
        usuario = nuevoUsuario
    }
}