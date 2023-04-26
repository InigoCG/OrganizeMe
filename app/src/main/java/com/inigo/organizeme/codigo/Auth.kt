package com.inigo.organizeme.codigo

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.inigo.organizeme.navegacion.AppPantallas

private lateinit var auth: FirebaseAuth

fun registrarUsuarios(context: Context, navController: NavController, email: String, password: String) {
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

fun login(context: Context, navController: NavController, email: String, password: String) {
    auth = Firebase.auth

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                navController.popBackStack()
                navController.navigate(AppPantallas.PantallaPrincipal.ruta)
            } else {
                Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
}