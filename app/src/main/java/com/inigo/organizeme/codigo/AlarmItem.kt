package com.inigo.organizeme.codigo

import java.time.LocalDateTime

data class AlarmItem(
    val fecha: LocalDateTime,
    val titulo: String,
    val mensaje: String
)
