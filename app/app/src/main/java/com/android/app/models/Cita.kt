package com.android.app.models

data class Cita(
    val id: Int,
    val titulo: String, // Razón o descripción de la cita (ej: "Revisión anual", "Dermatología")
    val fecha: String,  // Fecha de la cita (ej: "Nov. 22")
    val hora: String,   // Hora de la cita (ej: "08:00")
    val doctor: String, // Nombre del doctor o especialista (ej: "Dra. López")
    val idUsuario: Int  // Clave foránea para el usuario
)