package com.android.app

import Usuario
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import java.sql.PreparedStatement
import java.time.LocalDate

class Login : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
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

        // Lista donde se cargarán los usuarios de la BD
        val userList = mutableListOf<Usuario>()

        // -------- CONEXIÓN A LA BD Y CARGA DE USUARIOS --------
        val conn = ConecctionDB()
        val connect = conn.getConnection()

        val query = "SELECT id, nombre, contraseña FROM usuario"

        connect?.createStatement().use { stmt ->
            val rs = stmt?.executeQuery(query)

            if (rs != null) {
                while (rs.next()) {
                    val id = rs.getInt("id")
                    val nombre = rs.getString("nombre")
                    val contrasena = rs.getString("contraseña")

                    userList.add(Usuario(id, nombre, "", contrasena, LocalDate.now()))
                }
            }
        }

        // -------- LÓGICA DE LOGIN --------
        btnLogin.setOnClickListener {

            val nombre = inputNombre.text.toString().trim()
            val contra = inputContra.text.toString().trim()

            if (nombre.isEmpty() || contra.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Buscar usuario en la lista
            val usuario = userList.find { it.getNombre() == nombre }

            if (usuario == null) {
                Toast.makeText(this, "El nombre es incorrecto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (usuario.getContraseña() != contra) {
                Toast.makeText(this, "La contraseña es incorrecta", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Éxito → abrir siguiente pantalla
            Toast.makeText(this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("id_usuario", usuario.getId())
            startActivity(intent)

            finish() // Para que no vuelva al login al pulsar atrás
        }
    }
}
