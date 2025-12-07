package com.android.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.net.Uri
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.app.DB.SQLiteDBManager
import com.google.android.material.textfield.TextInputEditText
import com.android.app.models.Usuario
import com.android.app.views.NotificacionesMedicamento
import com.android.app.views.VistaMedico

class Login : AppCompatActivity() {

    private var userList = listOf<Usuario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val inputNombre = findViewById<TextInputEditText>(R.id.email)
        val inputContra = findViewById<TextInputEditText>(R.id.password)
        val btnLogin = findViewById<Button>(R.id.log_in)
        val btnSolicitar = findViewById<Button>(R.id.solicitar)

        val dbManager = SQLiteDBManager(this)
        userList = dbManager.obtenerTodosLosUsuarios()

        Log.d("LOGIN_ACTIVITY", "Total de usuarios cargados para el login: ${userList.size}")
        if (userList.isNotEmpty()) {
            Toast.makeText(this, "Bienvenido a Remi!", Toast.LENGTH_SHORT).show()
        }

        btnLogin.setOnClickListener {

            val nombreOEmail = inputNombre.text.toString().trim()
            val contra = inputContra.text.toString().trim()

            if (nombreOEmail.isEmpty() || contra.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val usuario = userList.find {
                (it.nombre == nombreOEmail || it.email == nombreOEmail)
            }

            if (usuario == null) {
                Toast.makeText(this, "Usuario o Contraseña incorrectos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (usuario.contrasena != contra) {
                Toast.makeText(this, "Usuario o Contraseña incorrectos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Inicio de sesión correcto. ¡Bienvenido ${usuario.nombre}!", Toast.LENGTH_SHORT).show()

            if (usuario.id==usuario.id_Medico){
                val intent = Intent(this, VistaMedico::class.java)
                intent.putExtra("id_usuario", usuario.id)
                startActivity(intent)
            }else {
                val intent = Intent(this, NotificacionesMedicamento::class.java)
                intent.putExtra("id_usuario", usuario.id)
                startActivity(intent)
            }
            finish()
        }

        btnSolicitar.setOnClickListener {
            val numero = "+34633311142"
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$numero")
            }
            Toast.makeText(this, "Solicitar cuenta al administrador", Toast.LENGTH_SHORT).show()
            startActivity(intent)

        }
    }
}