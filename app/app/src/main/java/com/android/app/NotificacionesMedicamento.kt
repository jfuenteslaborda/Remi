package com.android.app

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.app.models.Medicamento

class NotificacionesMedicamento : AppCompatActivity() {

    private var medicamentosList = mutableListOf<Medicamento>() // mutable para agregar
    private lateinit var contenedor: LinearLayout
    private lateinit var inflater: LayoutInflater
    private lateinit var dbManager: SQLiteDBManager
    private var idUsuario: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tratamientos_pacientes)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_tratamientos_pacientes)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        idUsuario = intent.getIntExtra("id_usuario", -1)
        dbManager = SQLiteDBManager(this)
        medicamentosList = dbManager.obtenerMedicamentosPorUsuario(idUsuario).toMutableList()

        contenedor = findViewById(R.id.contenedorMedicamentos)
        inflater = LayoutInflater.from(this)

        // Mostrar los medicamentos actuales
        mostrarMedicamentos()

        // Botón para agregar nuevo medicamento
        val btnNuevoMedicamento = findViewById<Button>(R.id.nuevoMedicamento)
        btnNuevoMedicamento.setOnClickListener {
            mostrarDialogNuevoMedicamento()
        }
    }

    private fun mostrarMedicamentos() {
        contenedor.removeAllViews() // Limpiar antes de agregar
        medicamentosList.forEach { m ->
            val itemView = inflater.inflate(R.layout.item_medicamento, contenedor, false)

            val estadoTextView = itemView.findViewById<TextView>(R.id.txtEstadoPastilla)
            val txtNombre = itemView.findViewById<TextView>(R.id.txtNombrePastilla)
            val txtDuracion = itemView.findViewById<TextView>(R.id.txtDuracion)
            val txtHorario1 = itemView.findViewById<TextView>(R.id.txtHorario1)
            val boton = itemView.findViewById<Button>(R.id.btnAccion)

            txtNombre.text = m.nombre
            txtDuracion.text = m.hora
            txtHorario1.text = m.descripcion
            estadoTextView.text = if (m.tomado) "Tomado" else "No tomado"

            boton.setOnClickListener {
                if (estadoTextView.text.toString() == "Tomado") {
                    Toast.makeText(this, "Ya está marcado como tomado", Toast.LENGTH_SHORT).show()
                } else {
                    estadoTextView.text = "Tomado"
                    m.tomado = true
                }
            }

            contenedor.addView(itemView)
        }
    }

    private fun mostrarDialogNuevoMedicamento() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_nuevo_medicamento, null)

        val edtNombre = dialogView.findViewById<EditText>(R.id.edtNombre)
        val edtDescripcion = dialogView.findViewById<EditText>(R.id.edtDescripcion)
        val edtHora = dialogView.findViewById<EditText>(R.id.edtHora)

        AlertDialog.Builder(this)
            .setTitle("Nuevo Medicamento")
            .setView(dialogView)
            .setPositiveButton("Agregar", null)
            .setNegativeButton("Cancelar", null)
            .create().apply {
                show()

                getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val nombre = edtNombre.text.toString().trim()
                    val descripcion = edtDescripcion.text.toString().trim()
                    val hora = edtHora.text.toString().trim()

                    if (nombre.isEmpty() || hora.isEmpty()) {
                        Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    val nuevoMedicamento = Medicamento(
                        id = 0,
                        nombre = nombre,
                        hora = hora,
                        descripcion = descripcion,
                        tomado = false,
                        idUsuario = idUsuario
                    )

                    // Guardar en BD
                    val newRowId = dbManager.insertarMedicamento(nuevoMedicamento)
                    if (newRowId != -1L) {
                        nuevoMedicamento.id = newRowId.toInt()
                        medicamentosList.add(nuevoMedicamento)
                        mostrarMedicamentos()
                        dismiss()
                        Toast.makeText(context, "Medicamento agregado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error al agregar medicamento", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}
