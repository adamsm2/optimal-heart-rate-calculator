package com.example.optimalheartratecalculator

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.optimalheartratecalculator.databinding.ActivitySettingsBinding
import java.util.*

class SettingsActivity : AppCompatActivity() {
    private var isNightMode = false
    private var animationsON = true
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivitySettingsBinding

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ContextWrapper(newBase.setAppLocale()))
    }

    private fun Context.setAppLocale() : Context {
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val language = sharedPreferences.getString("language", "en")
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return createConfigurationContext(config)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        updateModeSwitcher()
        updateAnimationsSwitcher()
        setListeners()
    }

    private fun changeLanguage(lang: String) {
        val langEditor = sharedPreferences.edit()
        langEditor.putString("language", lang)
        langEditor.apply()
    }

    private fun updateModeSwitcher() {
        isNightMode = sharedPreferences.getBoolean("night", false)
        binding.ModeSwitch.isChecked = !isNightMode
    }

    private fun updateAnimationsSwitcher() {
        animationsON = sharedPreferences.getBoolean("animations", true)
        binding.AnimationsSwitch.isChecked = animationsON
    }

    private fun setListeners() {
        binding.ModeSwitch.setOnCheckedChangeListener {view, isChecked ->
            val editor = sharedPreferences.edit()
            if(isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("night", false)
                editor.apply()
                isNightMode = false
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("night", true)
                editor.apply()
                isNightMode = true
            }
        }


        binding.AnimationsSwitch.setOnCheckedChangeListener {view, isChecked ->
            val editor = sharedPreferences.edit()
            if(isChecked) {
                editor.putBoolean("animations", true)
                editor.apply()
                animationsON = true
            } else {
                editor.putBoolean("animations", false)
                editor.apply()
                animationsON = false
            }
        }


        binding.FromSettingsToMainActivityButton.setOnClickListener{
            finish()
        }

        binding.ChangeLanguageToEnglish.setOnClickListener{
            if (sharedPreferences.getString("language", "en") == "en") {
                return@setOnClickListener
            }
            changeLanguage("en")
            recreate()
        }
        binding.ChangeLanguageToPolish.setOnClickListener{
            if (sharedPreferences.getString("language", "en") == "pl") {
                return@setOnClickListener
            }
            changeLanguage("pl")
            recreate()
        }
    }

}