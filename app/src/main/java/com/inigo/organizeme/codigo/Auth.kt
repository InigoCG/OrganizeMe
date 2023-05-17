package com.inigo.organizeme.codigo

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.inigo.organizeme.navegacion.AppPantallas

private lateinit var auth: FirebaseAuth
private lateinit var db: DatabaseReference

fun registrarUsuarios(
    context: Context,
    navController: NavController,
    email: String,
    password: String
) {
    auth = Firebase.auth

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Usuario registrado", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            } else {
                Toast.makeText(context, "Usuario ya registrado", Toast.LENGTH_SHORT).show()
            }
        }
}

fun login(
    context: Context,
    navController: NavController,
    email: String,
    password: String,
    sharedViewModel: SharedViewModel,
) {
    auth = Firebase.auth

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                var usuario: Usuario? = null
                db =
                    FirebaseDatabase.getInstance("https://tfginigocarrate-default-rtdb.europe-west1.firebasedatabase.app")
                        .getReference("usuarios")
                db.child(email.replace('.', '-')).get().addOnSuccessListener {
                    val map: HashMap<String, String> = it.value as HashMap<String, String>
                    // val mapListas: HashMap<String, MutableList<ListaTareas>> = it.value as HashMap<String, MutableList<ListaTareas>>
                    usuario = Usuario(email = map.get("email"), nombre = map.get("nombre")/*, listaTareas = mapListas.get("listaTareas")*/)

                    navController.popBackStack()
                    sharedViewModel.addUsuario(usuario!!)
                    navController.navigate(AppPantallas.PantallaPrincipal.ruta)
                }
            } else {
                Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT)
                    .show()
            }
        }
}

fun escribirUsuariosDB(email: String, nombre: String) {
    try {
        db =
            FirebaseDatabase.getInstance("https://tfginigocarrate-default-rtdb.europe-west1.firebasedatabase.app").reference

        val usuario = Usuario(email.trim(), nombre.trim())

        db.child("usuarios").child(usuario.email!!.replace('.', '-')).setValue(usuario)
    } catch (exception: Exception) {
        //TODO Hacer un cambio de actividad o un Toast
    }
}

fun escribirDatosUsuario(datosUsuario: Usuario) {
    try {
        db =
            FirebaseDatabase.getInstance("https://tfginigocarrate-default-rtdb.europe-west1.firebasedatabase.app").reference

        val usuario = Usuario(datosUsuario.email?.trim(), datosUsuario.nombre?.trim(), datosUsuario.listaTareas)

        db.child("usuarios").child(usuario.email!!.replace('.', '-')).setValue(usuario)
    } catch (exception: Exception) {
        //TODO Hacer un cambio de actividad o un Toast
    }
}