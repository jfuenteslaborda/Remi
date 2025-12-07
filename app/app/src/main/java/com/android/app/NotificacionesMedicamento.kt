package com.android.app

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.app.models.Medicamento

class NotificacionesMedicamento : AppCompatActivity() {

    private var medicamentosList = listOf<Medicamento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tratamientos_pacientes)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_tratamientos_pacientes)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val idUsuario = intent.getIntExtra("id_usuario", -1)
        val dbManager = SQLiteDBManager(this)
        medicamentosList = dbManager.obtenerMedicamentosPorUsuario(idUsuario)

        val contenedor = findViewById<LinearLayout>(R.id.contenedorMedicamentos)
        val inflater = LayoutInflater.from(this)
        val boton = findViewById<Button>(R.id.btnAccion)

        medicamentosList.forEach { m ->
            val itemView = inflater.inflate(R.layout.item_medicamento, contenedor, false)

            val estadoTextView = itemView.findViewById<TextView>(R.id.txtEstadoPastilla)
            val txtNombre = itemView.findViewById<TextView>(R.id.txtNombrePastilla)
            val txtDuracion = itemView.findViewById<TextView>(R.id.txtDuracion)
            val txtHorario1 = itemView.findViewById<TextView>(R.id.txtHorario1)
            val boton = itemView.findViewById<Button>(R.id.btnAccion)

            txtNombre.text = m.nombre
            txtDuracion.text = m.hora
            estadoTextView.text = if (m.tomado) "Tomado" else "No tomado"
            txtHorario1.text = m.descripcion

            boton.setOnClickListener {
                if (estadoTextView.text.toString() == "Tomado") {
                    Toast.makeText(this, "Ya está marcado como tomado", Toast.LENGTH_SHORT).show()
                } else {
                    estadoTextView.text = "Tomado"
                }
            }


            contenedor.addView(itemView)
        }

    }
}
