package com.android.app.models

data class Usuario(
    val id: Int,
    val nombre: String,
    val email: String,
    val contrasena: String,
    val fechaCreacion: String
)