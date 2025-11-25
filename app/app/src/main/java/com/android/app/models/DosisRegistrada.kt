package com.android.app.models

data class DosisRegistrada(
    val id: Int,
    val nombreMedicamento: String, // Nombre del medicamento tomado (ej: "Ibuprofeno")
    val horaToma: String,          // Hora exacta de la toma (ej: "18:35")
    val fechaToma: String,         // Fecha de la toma (podría ser solo el día, ej: "2023-11-25")
    val idUsuario: Int             // Clave foránea para el usuario
)