package com.android.app.models
enum class Rol {
    PACIENTE,
    MEDICO
}
data class Usuario(

    val id: Int,
    val nombre: String,
    val email: String,
    val contrasena: String,
    val fechaCreacion: String,
    val rol: Rol,
)