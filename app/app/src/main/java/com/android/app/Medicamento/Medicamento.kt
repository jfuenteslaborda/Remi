package com.android.app.Medicamento

data class Medicamento(
    val id: Int,
    val nombre: String,
    val dosis: String,
    val hora: String,
    val tomado: Boolean,
    val idUsuario: Int
)