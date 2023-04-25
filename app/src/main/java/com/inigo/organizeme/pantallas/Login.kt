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
import com.inigo.organizeme.R
import com.inigo.organizeme.codigo.filtrarUsuariosEmail
import com.inigo.organizeme.navegacion.AppPantallas
import com.inigo.organizeme.ui.theme.OrganizeMeTheme

@Composable
fun ContenidoLogin(navController: NavController) {

    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    OrganizeMeTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageLogoLogin()

            Column(
                modifier = Modifier
                    .padding(top = 10.dp, start = 5.dp, end = 5.dp)
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
                        value = email,
                        onValueChange = { email = it },
                        label = {
                            Text(
                                text = "Email", color = MaterialTheme.colors.onBackground
                            )
                        },
                        modifier = Modifier
                            .background(MaterialTheme.colors.onSecondary),
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
                        modifier = Modifier
                            .background(MaterialTheme.colors.onSecondary),
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
            }

            BotonLogin(email, password, context, navController)

            Divider(
                modifier = Modifier.padding(top = 100.dp),
                color = MaterialTheme.colors.secondary,
                thickness = 1.dp
            )
            Row(modifier = Modifier.padding(top = 10.dp)) {
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = "¿No tienes cuenta?",
                    color = MaterialTheme.colors.onBackground
                )
                TextButton(onClick = {
                    navController.navigate(route = AppPantallas.Registro.ruta)
                }) {
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = "Regístrate",
                        color = MaterialTheme.colors.primary
                    )
                }
            }
        }
    }
}

@Composable
fun ImageLogoLogin() {
    Box(modifier = Modifier.padding(top = 20.dp)) {
        val image: Painter = painterResource(id = R.drawable.logo)
        Image(painter = image, contentDescription = "")
    }
}

@Composable
fun BotonLogin(email: String, password: String, context: Context, navController: NavController) {
    Button(
        modifier = Modifier.padding(top = 70.dp),
        onClick = {
            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(context, "Campos vacíos", Toast.LENGTH_SHORT).show()
            } else {
                var resultadoEmail = filtrarUsuariosEmail(email)

                if (resultadoEmail) {
                    Toast.makeText(context, "Email correcto", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Email incorrecto", Toast.LENGTH_SHORT).show()
                }

                navController.popBackStack()
                navController.navigate(AppPantallas.PantallaPrincipal.ruta)
            }
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
    ) {
        Text(text = "INICIAR SESIÓN", color = MaterialTheme.colors.onSecondary)
    }
}