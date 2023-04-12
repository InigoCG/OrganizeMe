package com.inigo.organizeme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.inigo.organizeme.codigo.Usuario
import com.inigo.organizeme.navegacion.AppNavegacion

private lateinit var analytics: FirebaseAnalytics
private lateinit var db: DatabaseReference

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        analytics = Firebase.analytics
        super.onCreate(savedInstanceState)
        setContent {
            AppNavegacion()
        }
    }

    @Preview
    @Composable
    fun Preview() {
        AppNavegacion()
    }
}