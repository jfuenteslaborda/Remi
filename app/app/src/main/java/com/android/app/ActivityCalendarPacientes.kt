package com.android.app

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityCalendarPacientes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tratamientos_pacientes)

        val mainView = findViewById<View>(R.id.tratamientos)
        mainView?.let { v ->
            // Ajuste de insets para status bar y navigation bar
            ViewCompat.setOnApplyWindowInsetsListener(v) { _, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            // Obtenemos medicamentos desde la base de datos
            val dbManager = SQLiteDBManager(this)
            val idUsuario = intent.getIntExtra("id_usuario", -1)
            val medicamentosList = dbManager.obtenerMedicamentosPorUsuario(idUsuario)

            // Contenedor definido en activity_tratamientos_pacientes.xml
            val contenedor = findViewById<LinearLayout>(R.id.contenedorMedicamentos)

            // Inflamos una tarjeta por cada medicamento
            for (m in medicamentosList) {
                val cardView = layoutInflater.inflate(R.layout.item_medicamento, contenedor, false)

                val txtNombre = cardView.findViewById<TextView>(R.id.txtNombreMedicamento)
                val txtEstado = cardView.findViewById<TextView>(R.id.txtEstado)
                val txtHorarios = cardView.findViewById<TextView>(R.id.txtHorarios)
                val txtDuracion = cardView.findViewById<TextView>(R.id.txtDuracion)

                // Asignamos valores
                txtNombre.text = m.nombre
                txtEstado.text = if (m.tomado) "Tomado" else "Activo"
                txtHorarios.text = m.hora
                txtDuracion.text = m.hora

                // Insertamos la tarjeta en el contenedor
                contenedor.addView(cardView)
            }
        }
    }
}