package com.inigo.organizeme.codigo

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.inigo.organizeme.navegacion.AppPantallas
import java.text.SimpleDateFormat

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

@SuppressLint("SimpleDateFormat")
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
                    val map: HashMap<String, Any> = it.value as HashMap<String, Any>
                    val formatter = SimpleDateFormat("dd-MM-yyyy")
                    var listaValores: MutableList<ListaTareas>? = null
                    if (map.get("listaTareas") != null) {
                        val listAux = map.get("listaTareas") as MutableList<Any>
                        listaValores = mutableListOf()
                        for (i in listAux.indices) {
                            var aux = listAux[i] as HashMap<*, *>

                            var mapTareas: HashMap<String, Tarea> = it.child("listaTareas")
                                .child(i.toString()).value as HashMap<String, Tarea>
                            var listaValoresTareas: MutableList<Tarea>? = null
                            if (mapTareas.get("tareas") != null) {
                                val listTareasAux = mapTareas.get("tareas") as MutableList<Any>
                                listaValoresTareas = mutableListOf()
                                for (j in listTareasAux.indices) {
                                    var auxTareas = listTareasAux[j] as HashMap<*, *>
                                    var tareas = Tarea(
                                        auxTareas.get("nombre").toString(),
                                        auxTareas.get("descripcion").toString(),
                                        formatter.parse(auxTareas.get("fecha").toString())
                                    )
                                    listaValoresTareas.add(tareas)
                                }
                            }

                            var listaTareas = ListaTareas(
                                aux.get("nombre").toString(),
                                listaValoresTareas
                            )
                            listaValores.add(listaTareas)
                        }
                    }

                    usuario = Usuario(
                        email = map.get("email").toString(),
                        nombre = map.get("nombre").toString(),
                        listaTareas = listaValores
                    )

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

        val usuario = Usuario(
            datosUsuario.email?.trim(),
            datosUsuario.nombre?.trim(),
            datosUsuario.listaTareas
        )

        db.child("usuarios").child(usuario.email!!.replace('.', '-')).setValue(usuario)
    } catch (exception: Exception) {
        //TODO Hacer un cambio de actividad o un Toast
    }
}

fun escribirDatosListaTareas(datosUsuario: Usuario, listaTareas: ListaTareas, index: Int) {
    try {
        db =
            FirebaseDatabase.getInstance("https://tfginigocarrate-default-rtdb.europe-west1.firebasedatabase.app").reference

        val usuario = Usuario(
            datosUsuario.email?.trim(),
            datosUsuario.nombre?.trim(),
            datosUsuario.listaTareas
        )

        db.child("usuarios").child(usuario.email!!.replace('.', '-')).child("listaTareas").setValue(listaTareas)
    } catch (exception: Exception) {
        //TODO Hacer un cambio de actividad o un Toast
    }
}