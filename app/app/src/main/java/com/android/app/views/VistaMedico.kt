package com.android.app.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.app.DB.SQLiteDBManager
import com.android.app.R

class VistaMedico : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.vista_medico)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vistaMedico)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val idMedico = intent.getIntExtra("id_usuario", -1)
        val dbManager = SQLiteDBManager(this)

        val usuariosList = dbManager.obtenerUsuarioPorMedico(idMedico)

        val contenedor = findViewById<LinearLayout>(R.id.contenedorUsuarios)
        val inflater = LayoutInflater.from(this)

        usuariosList.forEach { u ->
            if (u.id!=u.id_Medico){
                val itemView = inflater.inflate(R.layout.item_user, contenedor, false)

                val txtNombre = itemView.findViewById<TextView>(R.id.nombreUsuario)
                val txtDescripcion = itemView.findViewById<TextView>(R.id.descripcionUsuario)
                val btn = itemView.findViewById<Button>(R.id.btnEntrar)
                val intent = Intent(this, NotificacionesMedicamentoMedico::class.java)
                intent.putExtra("id_usuario", u.id)

                txtNombre.text = u.nombre
                txtDescripcion.text = u.descripcion

                contenedor.addView(itemView)

                btn.setOnClickListener {
                    startActivity(intent)
                }
            }
        }


    }
}