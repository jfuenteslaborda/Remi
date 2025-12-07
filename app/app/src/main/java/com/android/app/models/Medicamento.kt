package com.android.app.models

data class Medicamento(
    var id: Int,
    val nombre: String,
    val hora: String,
    val descripcion: String,
    var tomado: Boolean,
    val idUsuario: Int
)