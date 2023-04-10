package com.inigo.organizeme.pantallas

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inigo.organizeme.MainActivity
import com.inigo.organizeme.R
import com.inigo.organizeme.ui.theme.OrganizeMeTheme
import java.util.regex.Pattern

@Composable
fun ContenidoRegistro(navController: NavController) {

    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var repeatPassword by rememberSaveable { mutableStateOf("") }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var repeatPasswordVisible by rememberSaveable { mutableStateOf(false) }

    OrganizeMeTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageLogoRegistro()

            Column(
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .fillMaxWidth()
                    .wrapContentSize()
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth()
                        .wrapContentSize()
                ) {

                    TextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = {
                            Text(
                                text = "Nombre", color = MaterialTheme.colors.onBackground
                            )
                        },
                        modifier = Modifier.background(MaterialTheme.colors.onSecondary),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = MaterialTheme.colors.primaryVariant,
                            cursorColor = MaterialTheme.colors.primaryVariant
                        )
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth()
                        .wrapContentSize()
                ) {

                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = {
                            Text(
                                text = "Email", color = MaterialTheme.colors.onBackground
                            )
                        },
                        modifier = Modifier.background(MaterialTheme.colors.onSecondary),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = MaterialTheme.colors.primaryVariant,
                            cursorColor = MaterialTheme.colors.primaryVariant
                        )
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth()
                        .wrapContentSize()
                ) {

                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = {
                            Text(
                                text = "Contraseña", color = MaterialTheme.colors.onBackground
                            )
                        },
                        modifier = Modifier.background(MaterialTheme.colors.onSecondary),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = MaterialTheme.colors.primaryVariant,
                            cursorColor = MaterialTheme.colors.primaryVariant
                        ),
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff

                            val description =
                                if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, description, tint = MaterialTheme.colors.onBackground)
                            }
                        }
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth()
                        .wrapContentSize()
                ) {

                    TextField(
                        value = repeatPassword,
                        onValueChange = { repeatPassword = it },
                        label = {
                            Text(
                                text = "Repetir contraseña",
                                color = MaterialTheme.colors.onBackground
                            )
                        },
                        modifier = Modifier.background(MaterialTheme.colors.onSecondary),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = MaterialTheme.colors.primaryVariant,
                            cursorColor = MaterialTheme.colors.primaryVariant
                        ),
                        singleLine = true,
                        visualTransformation = if (repeatPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if (repeatPasswordVisible)
                                Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff

                            val description =
                                if (repeatPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"

                            IconButton(onClick = { repeatPasswordVisible = !repeatPasswordVisible }) {
                                Icon(imageVector = image, description, tint = MaterialTheme.colors.onBackground)
                            }
                        }
                    )
                }
            }

            BotonRegistro(nombre, email, password, repeatPassword, context, navController)

            Divider(
                modifier = Modifier.padding(top = 30.dp),
                color = MaterialTheme.colors.secondary,
                thickness = 1.dp
            )
            Row(modifier = Modifier.padding(top = 10.dp)) {
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = "¿Ya tienes una cuenta?",
                    color = MaterialTheme.colors.onBackground
                )
                TextButton(onClick = {
                    navController.popBackStack()
                }) {
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = "Inicia sesión",
                        color = MaterialTheme.colors.primary
                    )
                }
            }
        }
    }
}

@Composable
fun ImageLogoRegistro() {
    Box(modifier = Modifier.padding(top = 20.dp)) {
        val image: Painter = painterResource(id = R.drawable.logo)
        Image(painter = image, contentDescription = "")
    }
}

@Composable
fun BotonRegistro(
    nombre: String,
    email: String,
    password: String,
    repeatPassword: String,
    context: Context,
    navController: NavController
) {
    Button(
        modifier = Modifier.padding(top = 40.dp),
        onClick = {
            if (nombre.isBlank() || email.isBlank() || password.isBlank() || repeatPassword.isBlank()) {
                Toast.makeText(context, "Campos vacíos", Toast.LENGTH_SHORT).show()
            } else {
                if (!validarEmail(email)) {
                    Toast.makeText(context, "Formato email incorrecto", Toast.LENGTH_SHORT).show()
                } else if (password.length < 5) {
                    Toast.makeText(context, "Contraseña demasiado corta", Toast.LENGTH_SHORT).show()
                } else if (repeatPassword != password) {
                    Toast.makeText(context, "Las contraseñas no son iguales", Toast.LENGTH_SHORT)
                        .show()
                } else {


                    Toast.makeText(context, "Usuario registrado", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            }
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
    ) {
        Text(text = "REGISTRARSE", color = MaterialTheme.colors.onSecondary)
    }
}

fun validarEmail(email: String): Boolean {
    val pattern = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$"
    )
    val matcher = pattern.matcher(email)

    return matcher.matches()
}