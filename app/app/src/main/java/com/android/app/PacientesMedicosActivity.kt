package com.android.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.android.app.R

import com.android.app.testing_calendar_nav_bar_pacientes
import com.android.app.HomeFragment
import com.android.app.TreatmentsFragment
import com.android.app.NotificationsFragment

class PacientesMedicoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pacientes_medico)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

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
                R.id.menuCalendario -> testing_calendar_nav_bar_pacientes()
                R.id.menuNotificaciones -> NotificationsFragment()

                else -> return@setOnItemSelectedListener false
            }

            loadFragment(selectedFragment)
            true
        }
    }
}