package com.example.optimalheartratecalculator

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.ConfigurationCompat
import com.example.optimalheartratecalculator.databinding.ActivityMainBinding
import com.example.optimalheartratecalculator.databinding.ActivitySettingsBinding
import java.util.*

class SettingsActivity : AppCompatActivity() {
    private var isNightMode = false
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var languageSharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var langEditor: SharedPreferences.Editor
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        loadSharedPreferences()
        setListeners()
    }

    private fun changeLanguage(lang: String) {
        MainActivity.shouldRecreate = true

        langEditor = languageSharedPreferences.edit()
        langEditor.putString("language", lang)
        langEditor.apply()

        //val locale = Locale(lang)
        //Locale.setDefault(locale)
        //val config = Configuration()
        /* ========================================================== */
        /* doczytac configuration compat itp */
        /* ========================================================== */
        //ConfigurationCompat.setLocale(config, locale)
        //baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }

    private fun loadSharedPreferences() {
        loadLanguage()
        loadMode()
    }

    private fun loadMode() {
        sharedPreferences = getSharedPreferences("mode", Context.MODE_PRIVATE)
        isNightMode = sharedPreferences.getBoolean("night", false)
        binding.ModeSwitch.isChecked = !isNightMode
    }

    private fun loadLanguage() {
        languageSharedPreferences = getSharedPreferences("language", Context.MODE_PRIVATE)
        var language = languageSharedPreferences.getString("language", "en")
        var locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(Locale(language))

        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

    }

    private fun setListeners() {
        binding.ModeSwitch.setOnClickListener{
            MainActivity.shouldRecreate = false
            /* ================================================== */
            if(isNightMode) {                                /*binding.ModeSwitch.isChecked*/
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) /* ====================*/
                editor = sharedPreferences.edit()
                editor.putBoolean("night", false)
                editor.apply()
                isNightMode = false
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor = sharedPreferences.edit()
                editor.putBoolean("night", true)
                editor.apply()
                isNightMode = true
            }
        }

        binding.FromSettingsToMainActivityButton.setOnClickListener{
            finish()
        }

//        binding.button.setOnClickListener{
//            binding.TestowyText.text = sharedPreferences.getBoolean("night", false).toString()
//        }

        binding.ChangeLanguageToEnglish.setOnClickListener{
            if (languageSharedPreferences.getString("language", "en") == "en") {
                return@setOnClickListener
            }
            changeLanguage("en")
            recreate()
        }
        binding.ChangeLanguageToPolish.setOnClickListener{
            if (languageSharedPreferences.getString("language", "en") == "pl") {
                return@setOnClickListener
            }
            changeLanguage("pl")
            recreate()
        }
    }

}