package com.inigo.organizeme.codigo

import android.Manifest
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.inigo.organizeme.R
import java.time.format.DateTimeFormatter

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val titulo = intent.getStringExtra("titulo")
        val mensaje = intent.getStringExtra("mensaje")
        println("probando $titulo")

        buildNotification(context, titulo!!, mensaje!!)
    }

    fun buildNotification(context: Context, titulo: String, mensaje: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification =
            NotificationCompat.Builder(context, "channel_id")
                .setContentText(mensaje)
                .setContentTitle(titulo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .build()
        notificationManager.notify(1, notification)
    }

    fun permisoNotificacion(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else mutableStateOf(true)


    }

    @Composable
    fun notificaciones(context: Context) {
        var hasNotificationPermission by remember {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                mutableStateOf(
                    ContextCompat.checkSelfPermission(
                        context, Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                )
            } else mutableStateOf(true)
        }

        @Composable
        fun notificationPermission(context: Context): ManagedActivityResultLauncher<String, Boolean> {
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    hasNotificationPermission = isGranted
                    if (!isGranted) {
                        // ActivityCompat.shouldShowRequestPermissionRationale()
                    }
                }
            )
            return permissionLauncher
        }

        fun showNotification(context: Context) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification =
                NotificationCompat.Builder(context, "channel_id")
                    .setContentText("Contenido")
                    .setContentTitle("Título")
                    .setSmallIcon(R.drawable.logo)
                    .build()
            notificationManager.notify(1, notification)
        }

        var permiso = notificationPermission(context = context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permiso.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (hasNotificationPermission) {
            showNotification(context)
        }
    }
}