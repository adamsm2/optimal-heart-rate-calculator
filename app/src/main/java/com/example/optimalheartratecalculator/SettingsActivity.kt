package com.example.optimalheartratecalculator

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.optimalheartratecalculator.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private var isNightMode = false
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.hide()


        var binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE)
        //isNightMode = sharedPreferences.getBoolean("night", false)
        //binding.ModeSwitch.isChecked = isNightMode


        binding.ModeSwitch.setOnClickListener{
            if(isNightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                //editor = sharedPreferences.edit()
                //editor.putBoolean("night", false)
                isNightMode = false
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                //editor = sharedPreferences.edit()
                //editor.putBoolean("night", true)
                isNightMode = true
            }
        }

        binding.FromSettingsToMainActivityButton.setOnClickListener{
            finish()
        }

    }
}