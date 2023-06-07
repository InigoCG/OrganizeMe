package com.inigo.organizeme.codigo

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.Date

class AndroidAlarmScheduler(
    private val context: Context,
    private val ldt: LocalDateTime
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: AlarmItem) {
        var timeInMillis = item.fecha.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("titulo", item.titulo)
            putExtra("mensaje", item.mensaje)
        }

        // var fecha: Long = ldt.toInstant(ZoneOffset.UTC).toEpochMilli()

        /*val zdt: ZonedDateTime = ZonedDateTime.of(ldt, ZoneId.systemDefault())
        val fecha: Long = zdt.toInstant().toEpochMilli()*/

        val fecha = LocalDateTime.now().plusSeconds(30)

        /*val zoneId: ZoneId = ZoneId.systemDefault()
        val date: Date = Date.from(ldt.atZone(zoneId).toInstant())
        var calendar: Calendar = Calendar.getInstance()
        calendar.time = date*/

        println("esta es la fecha -> " + item.fecha)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        )
    }

    override fun cancel(item: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        )
    }

}