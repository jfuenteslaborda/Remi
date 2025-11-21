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

        //Conexión a la base de datos
        val conn = ConecctionDB();
        val connect = conn.getConnection();
        val insertQuery = "SELECT id, nombre, contraseña FROM usuario;"

        //Lógica
        var nombre = findViewById<TextInputEditText>(R.id.email)
        var contra = findViewById<TextInputEditText>(R.id.password)
        val log_in = findViewById<Button>(R.id.log_in)
        val userList = mutableListOf<Usuario>()


        connect?.createStatement().use { stmt -> val rs = stmt?.executeQuery(insertQuery)

            if (rs != null) {
                while (rs.next()){
                    val id = rs.getInt("id")
                    val nombre = rs.getString("nombre")
                    val contrasena = rs.getString("contraseña")

                    userList.add(Usuario(id, nombre, "", contrasena, LocalDate.now()))

                }
            }
        }

        log_in.setOnClickListener {
            for (n in userList){
                if (nombre.equals(n.getNombre())){
                    if(nombre.equals(n.getNombre()) && contra.equals(n.getContraseña())){

                    } else {
                        var intent = Intent(this, Login::class.java)
                        Toast.makeText(this, "La contraseña es incorrecta", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                    }

                } else {
                    var intent2 = Intent(this, Login::class.java)
                    Toast.makeText(this, "El nombre es incorrecto", Toast.LENGTH_SHORT).show()
                    startActivity(intent2)

                }
            }
        }








    }
}