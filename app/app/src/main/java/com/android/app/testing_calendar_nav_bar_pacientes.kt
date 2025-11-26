package com.android.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class testing_calendar_nav_bar_pacientes : Fragment() {

    // Este es el método que infla el layout XML para el Fragmento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 🔑 NECESITAS REEMPLAZAR 'activity_calendar_pacientes' con el nombre CORRECTO
        // que Android Studio le da a tu archivo XML del calendario
        return inflater.inflate(R.layout.activity_paciente_pacientes, container, false)
    }
}