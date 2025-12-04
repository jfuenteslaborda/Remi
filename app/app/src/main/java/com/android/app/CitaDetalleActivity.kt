package com.android.app.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.app.R
import com.android.app.SQLiteDBManager

// Asegúrate de crear el layout 'activity_cita_detalle.xml'
class CitaDetalleActivity : AppCompatActivity() {

    private lateinit var dbManager: SQLiteDBManager
    private var citaId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cita_detalle)

        dbManager = SQLiteDBManager(this)

        // 1. Obtener el ID que viene del Intent
        citaId = intent.getIntExtra(EXTRA_CITA_ID, -1)

        if (citaId != -1) {
            cargarDetalleCita(citaId)
        } else {
            Toast.makeText(this, "Error: Cita no especificada.", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun cargarDetalleCita(id: Int) {
        val cita = dbManager.obtenerCitaPorId(id)

        if (cita != null) {
            // Asignación de datos (AJUSTA LOS IDs DE TUS TEXTVIEWS)
            findViewById<TextView>(R.id.txtTituloDetalle).text = cita.titulo
            findViewById<TextView>(R.id.txtDoctorDetalle).text = "Doctor(a): ${cita.doctor}"
            findViewById<TextView>(R.id.txtFechaHoraDetalle).text = "${cita.fecha} a las ${cita.hora}"
            findViewById<TextView>(R.id.txtEstadoDetalle).text = "Estado: ${cita.estado.name}"
        } else {
            Toast.makeText(this, "Cita no encontrada en la base de datos.", Toast.LENGTH_LONG).show()
        }
    }

    // Usado como clave de Intent en activity_calendar_pacientes.kt
    companion object {
        const val EXTRA_CITA_ID = "cita_id"
    }
}