package com.android.app.models

data class Medicamento(
    val id: Int,
    val nombre: String,
    val hora: String,
    val descripcion: String,
    val tomado: Boolean,
    val idUsuario: Int
)