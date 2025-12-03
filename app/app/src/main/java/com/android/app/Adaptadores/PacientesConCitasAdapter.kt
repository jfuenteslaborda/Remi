package com.android.app.Adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.android.app.R
import com.android.app.models.Pacientes

class PacienteConCitasAdapter(
    context: Context,
    private val lista: List<Pacientes>
) : ArrayAdapter<Pacientes>(context, 0, lista) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val paciente = getItem(position)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_paciente, parent, false)

        val txtNombre = view.findViewById<TextView>(R.id.txtNombre)
        val txtFecha = view.findViewById<TextView>(R.id.txtFechaNacimiento)
        val txtCitas = view.findViewById<TextView>(R.id.txtNumCitas)

        txtNombre.text = paciente?.nombre
        txtFecha.text = "Fecha nacimiento: ${paciente?.fechaNacimiento}"
        txtCitas.text = "Citas realizadas: ${paciente?.numCitas}"

        return view
    }
}
