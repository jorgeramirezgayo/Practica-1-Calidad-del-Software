package com.example.easynotes.View.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.easynotes.R
import android.widget.RadioGroup
import android.widget.RadioButton
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import android.content.Context



/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Referencias a los RadioButton
        val rbModoOscuro = view.findViewById<RadioButton>(R.id.rbModoOscuro)
        val rbModoClaro = view.findViewById<RadioButton>(R.id.rbModoClaro)

        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES


        if (isDarkMode) {
            rbModoOscuro.isChecked = true
            rbModoClaro.isChecked = false
        } else {
            rbModoOscuro.isChecked = false
            rbModoClaro.isChecked = true
        }



        // Agrega un listener al RadioGroup para manejar los eventos de los RadioButton
        val grupoModoOscuro = view.findViewById<RadioGroup>(R.id.grupoModoOscuro)
        grupoModoOscuro.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbModoOscuro -> enableDarkMode(requireActivity() as AppCompatActivity)
                R.id.rbModoClaro -> disableDarkMode(requireActivity() as AppCompatActivity)
            }
        }

        return view
    }

    private fun enableDarkMode(activity: AppCompatActivity) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        activity.delegate.applyDayNight()
    }

    private fun disableDarkMode(activity: AppCompatActivity) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        activity.delegate.applyDayNight()
    }
}

