package com.android.app // Asegúrate de usar tu paquete real

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.android.app.R // Para acceder a tus recursos (layouts, IDs, etc.)
// 🚨 Importa tu clase Fragmento (testing_calendar_nav_bar_pacientes)
import com.android.app.testing_calendar_nav_bar_pacientes

class PacientesMedicoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Relacionar esta Actividad con el layout de la Nav-Bar
        setContentView(R.layout.activity_pacientes_medico)

        // 2. Cargar el Fragmento de calendario por defecto
        if (savedInstanceState == null) {
            // Reemplaza el contenido del FragmentContainerView (R.id.fragment_container)
            // con una nueva instancia de tu Fragmento de calendario.
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, testing_calendar_nav_bar_pacientes())
                .commit()
        }

        // 3. Configurar la lógica de la barra de navegación (BottomNavigationView)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

    }
}