package com.example.optimalheartratecalculator

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.NumberPicker
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.optimalheartratecalculator.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var maleIsSet: Boolean? = null
    private var age = 1

    companion object {
        var shouldRecreate = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        loadSharedPreferences()
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        if (shouldRecreate) {
            shouldRecreate = false
            recreate()
        }
    }

    private fun loadSharedPreferences() {
        loadLanguage()
        loadMode()
    }

    private fun loadMode() {
        var sharedPreferences = getSharedPreferences("mode", Context.MODE_PRIVATE)
        var nightMode = sharedPreferences.getBoolean("night", false)
        if(nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            AppCompatDelegate.getDefaultNightMode()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun loadLanguage() {
        var languageSharedPreferences = getSharedPreferences("language", Context.MODE_PRIVATE)
        var language = languageSharedPreferences.getString("language", "en")
        var locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(Locale(language))

        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

    }

    /*private fun loadSharedPreferences() {
        sharedPreferences = getSg
    }

     */
    private fun setListeners() {
        binding.MaleRelativeLayout.setOnClickListener{
            maleIsSet = true
            if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                binding.MaleRelativeLayout.setBackgroundResource(R.drawable.night_mode_after_click)
                binding.FemaleRelativeLayout.setBackgroundResource(R.drawable.night_mode_backgrounds)
            } else {
                binding.MaleRelativeLayout.setBackgroundResource(R.drawable.after_click)
                binding.FemaleRelativeLayout.setBackgroundResource(R.drawable.backgrounds)
            }
        }

        binding.FemaleRelativeLayout.setOnClickListener{
            maleIsSet = false
            if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                binding.FemaleRelativeLayout.setBackgroundResource(R.drawable.night_mode_after_click)
                binding.MaleRelativeLayout.setBackgroundResource(R.drawable.night_mode_backgrounds)
            } else {
                binding.FemaleRelativeLayout.setBackgroundResource(R.drawable.after_click)
                binding.MaleRelativeLayout.setBackgroundResource(R.drawable.backgrounds)
            }
        }

        binding.CalculateButton.setOnClickListener{
            if (maleIsSet == null) {
                return@setOnClickListener
            }
            binding.CalculationResult.text = if (maleIsSet as Boolean) "%.1f".format(0.5 * (226 - age)) + " - " + "%.1f".format(0.85 * (226 - age))  + "\n" + getString(R.string.BPM) else "%.1f".format(0.5 * (220 - age)) + " - " + "%.1f".format(0.85 * (220 - age))  + "\n" + getString(R.string.BPM)
        }

        binding.FromMainToSettingsActivityButton.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        var tvAge = binding.Age
        val numberPicker = binding.NumberPicker

        numberPicker.minValue = 1
        numberPicker.maxValue = 100
        tvAge.text = getString(R.string.age) + String.format(" ${numberPicker.value}")

        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            age = newVal
            tvAge.text = getString(R.string.age) + " $newVal"
        }
    }
}