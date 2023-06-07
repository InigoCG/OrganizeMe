package com.inigo.organizeme.codigo

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppBar(
    nombre: String,
    onNavigationIconClick: () -> Unit
) {
    TopAppBar(title = {
        Text(
            modifier = Modifier.padding(start = 10.dp),
            fontSize = 20.sp,
            text = nombre,
            color = MaterialTheme.colors.onSecondary
        )
    }, backgroundColor = MaterialTheme.colors.primaryVariant, navigationIcon = {
        IconButton(onClick = onNavigationIconClick) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "botón de menú",
                modifier = Modifier.size(30.dp),
                tint = MaterialTheme.colors.onSecondary
            )
        }
    })
}