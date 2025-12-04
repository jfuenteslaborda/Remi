package com.android.app

import android.annotation.SuppressLint
import android.content.Intent // ⬅️ AÑADIDO
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.app.models.estado
import com.android.app.ui.CitaDetalleActivity // ⬅️ AÑADIDO (Ajustar paquete si es necesario)
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class activity_calendar_pacientes : AppCompatActivity() {

    private lateinit var dbManager: SQLiteDBManager
    private lateinit var layoutProgramada: LinearLayout
    private lateinit var layoutTomada: LinearLayout
    private lateinit var layoutNoTomada: LinearLayout
    private lateinit var layoutCitas: LinearLayout
    private val ID_USUARIO_ACTUAL = 2 // Paciente de prueba (jfuentes)

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_pacientes)

        dbManager = SQLiteDBManager(this)

        val mainView = findViewById<View>(R.id.main)

        layoutProgramada = findViewById(R.id.linearLayoutProgramada)
        layoutTomada = findViewById(R.id.linearLayoutTomada)
        layoutNoTomada = findViewById(R.id.linearLayoutNoTomada)
        layoutCitas = findViewById(R.id.linearLayoutCitas)

        mainView?.let { v ->
            ViewCompat.setOnApplyWindowInsetsListener(v) { _, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        establecerFechaActual()
        cargarDatosDeMedicacion(ID_USUARIO_ACTUAL)
        cargarDatosDeCitas(ID_USUARIO_ACTUAL)
    }

    override fun onResume() {
        super.onResume()
        // Refrescar datos cada vez que se regresa a esta pantalla
        cargarDatosDeMedicacion(ID_USUARIO_ACTUAL)
        cargarDatosDeCitas(ID_USUARIO_ACTUAL)
    }

    private fun establecerFechaActual() {
        val txtFecha = findViewById<TextView>(R.id.txtCurrentDate)
        val formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
        txtFecha.text = LocalDate.now().format(formatter)
    }

    private fun cargarDatosDeMedicacion(userId: Int) {
        layoutProgramada.removeAllViews()
        layoutTomada.removeAllViews()
        layoutNoTomada.removeAllViews()
        val medicamentos = dbManager.obtenerMedicamentosPorUsuario(userId)
        val horaActual = LocalTime.now()

        for (medicamento in medicamentos) {
            val cardView = LayoutInflater.from(this).inflate(R.layout.card_medicamento_item, null, false)
            val txtNombre = cardView.findViewById<TextView>(R.id.txtNombreDinamico)
            val txtHora = cardView.findViewById<TextView>(R.id.txtHoraDinamico)
            val colorBar = cardView.findViewById<View>(R.id.colorBar)

            txtNombre.text = "${medicamento.nombre} ${medicamento.dosis}"
            txtHora.text = medicamento.hora

            if (medicamento.tomado) {
                txtNombre.text = "${medicamento.nombre} - Tomada"
                colorBar.background = ContextCompat.getDrawable(this, R.drawable.green_bar_calendar_patients)
                layoutTomada.addView(cardView)
            } else {
                val horaProgramada: LocalTime? = try { LocalTime.parse(medicamento.hora) } catch (e: Exception) { null }
                val debeClasificarseComoNoTomada = horaProgramada != null && horaActual.isAfter(horaProgramada)

                if (debeClasificarseComoNoTomada) {
                    txtNombre.text = "${medicamento.nombre} - NO Tomada"
                    colorBar.background = ContextCompat.getDrawable(this, R.drawable.red_bar_calendar_patients)
                    layoutNoTomada.addView(cardView)
                } else {
                    txtNombre.text = medicamento.nombre
                    colorBar.background = ContextCompat.getDrawable(this, R.drawable.blue_bar_calendar_pacientes)
                    layoutProgramada.addView(cardView)
                }

                cardView.setOnClickListener {
                    val filasAfectadas = dbManager.marcarMedicamentoComoTomado(medicamento.id)
                    if (filasAfectadas > 0) {
                        cargarDatosDeMedicacion(userId)
                    }
                }
            }
        }
    }

    private fun cargarDatosDeCitas(userId: Int) {
        layoutCitas.removeAllViews()
        val citas = dbManager.obtenerTodasLasCitasPorUsuario(userId)

        for (cita in citas) {
            val cardView = LayoutInflater.from(this).inflate(R.layout.card_cita_item, null, false)
            val txtTitulo = cardView.findViewById<TextView>(R.id.txtCitaTitulo)
            val txtHoraFecha = cardView.findViewById<TextView>(R.id.txtCitaHoraFecha)
            val txtDoctor = cardView.findViewById<TextView>(R.id.txtCitaDoctor)
            val colorBar = cardView.findViewById<View>(R.id.citaColorBar)

            txtTitulo.text = cita.titulo
            txtHoraFecha.text = "${cita.fecha} a las ${cita.hora}"
            txtDoctor.text = "Doctor(a): ${cita.doctor} - Estado: ${cita.estado.name}"

            when (cita.estado) {
                estado.Rechazada -> {
                    colorBar.background = ContextCompat.getDrawable(this, R.drawable.red_bar_calendar_patients)
                }
                else -> { /* Aceptada o Pendiente */ }
            }

            // ⭐️ LÓGICA DE CLICK CON SINTAXIS CORREGIDA ⭐️
            cardView.setOnClickListener {
                // Usamos this@Activity para asegurar el contexto correcto
                val intent = Intent(this@activity_calendar_pacientes, CitaDetalleActivity::class.java).apply {
                    // Usamos la constante definida en CitaDetalleActivity para la clave
                    putExtra(CitaDetalleActivity.EXTRA_CITA_ID, cita.id)
                }
                startActivity(intent)
            }

            layoutCitas.addView(cardView)
        }
    }
}