package com.inigo.organizeme.codigo

import android.content.Context
import com.google.firebase.database.*
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

fun filtrarUsuariosEmail(email: String): Boolean {
    var resultado: Boolean = false
    try {
        db = FirebaseDatabase.getInstance("https://tfginigocarrate-default-rtdb.europe-west1.firebasedatabase.app").getReference("usuarios")

//        db.child("usuarios").get().addOnSuccessListener {
//            for (usuario in it.children) {
//
//            }
//        }

        val filtrado = leerListaUsuarios()

        for (i in filtrado) {
            if (i.email.contains(email)) {
                resultado = true
            }
        }
    } catch (exception: Exception) {
        //TODO Hacer un cambio de actividad o un Toast
    }
    return resultado
}

fun leerListaUsuarios(): ArrayList<Usuario> {
    var listaUsuarios = ArrayList<Usuario>()
    try {
        db = FirebaseDatabase.getInstance("https://tfginigocarrate-default-rtdb.europe-west1.firebasedatabase.app").getReference("usuarios")

        db.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val usuario = i.getValue(Usuario :: class.java)
                        if (usuario != null) {
                            listaUsuarios.add(usuario)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    } catch (exception: Exception) {
        //TODO Hacer un cambio de actividad o un Toast
    }
    return listaUsuarios
}