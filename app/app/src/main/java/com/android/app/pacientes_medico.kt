package com.android.app

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.app.Adaptadores.PacienteConCitasAdapter

import com.google.android.material.bottomnavigation.BottomNavigationView

class pacientes_medico : AppCompatActivity() {

    private lateinit var dbManager: SQLiteDBManager
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pacientes_medico)

        dbManager = SQLiteDBManager(this)
        listView = findViewById(R.id.listViewPacientes)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // --- Obtener pacientes con número de citas ---
        val pacientes = dbManager.obtenerPacientesConCitas()

        // --- Asignar adaptador ---
        val adapter = PacienteConCitasAdapter(this, pacientes)
        listView.adapter = adapter

        // --- Función para cargar fragments ---
        fun loadFragment(fragment: Fragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.menuHome -> HomeFragment()
                R.id.menuTratamientos -> TreatmentsFragment()
                R.id.menuCalendario -> TestingCalendarNavBarPacientesRodrigo()
                R.id.menuNotificaciones -> NotificationsFragment()
                else -> return@setOnItemSelectedListener false
            }
            loadFragment(selectedFragment)
            true
        }
    }
}
