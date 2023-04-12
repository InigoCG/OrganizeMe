package com.inigo.organizeme.codigo

import androidx.compose.ui.platform.LocalContext
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private lateinit var db: DatabaseReference

fun escribirUsuariosDB(nombre: String, email: String, password: String) {
    try {
        db = FirebaseDatabase.getInstance("https://tfginigocarrate-default-rtdb.europe-west1.firebasedatabase.app").reference

        val usuario = Usuario(nombre.trim(), email.trim(), password.trim())

        db.child("usuarios").child(usuario.email.replace('.', '-')).setValue(usuario)
    } catch (exception: Exception) {
        //TODO Hacer un cambio de actividad o un Toast
    }
}

fun leerEmailUsuariosDB(email: String) {
    try {
        db = FirebaseDatabase.getInstance("https://tfginigocarrate-default-rtdb.europe-west1.firebasedatabase.app").reference

        db.child("usuarios").get().addOnSuccessListener {
            for (usuario in it.children) {
                /*if (usuario) {

                }*/
            }
        }
    } catch (exception: Exception) {
        //TODO Hacer un cambio de actividad o un Toast
    }
}