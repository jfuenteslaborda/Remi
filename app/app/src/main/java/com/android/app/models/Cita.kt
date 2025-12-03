package com.android.app.models


enum class estado {
    Aceptada,
    Pendiente,
    Rechazada
}
data class Cita(
    val id: Int,
    val titulo: String,
    val fecha: String,
    val hora: String,
    val doctor: String,
    val estado: estado,
    val idUsuario: Int
)