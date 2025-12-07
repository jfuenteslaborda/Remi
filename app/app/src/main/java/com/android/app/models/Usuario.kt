package com.android.app.models

data class Usuario(
    val id: Int,
    val nombre: String,
    val email: String,
    val descripcion:String,
    val contrasena: String,
    val fechaCreacion: String,
    val id_Medico: Int
)