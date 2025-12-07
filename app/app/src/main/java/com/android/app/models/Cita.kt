package com.android.app.models

data class Cita(
    val id: Int,
    val titulo: String,
    val fecha: String,
    val hora: String,
    val doctor: String,
    val idUsuario: Int
)