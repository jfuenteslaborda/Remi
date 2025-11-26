package com.android.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    companion object {
        private const val ARG_ES_MEDICO = "es_medico"

        fun newInstance(esMedico: Boolean): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putBoolean(ARG_ES_MEDICO, esMedico)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val esMedico = arguments?.getBoolean(ARG_ES_MEDICO, false) ?: false

        val layoutId = if (esMedico) {
            R.layout.activity_home_medico
        } else {
            R.layout.activity_home_pacientes
        }

        return inflater.inflate(layoutId, container, false)
    }
}
