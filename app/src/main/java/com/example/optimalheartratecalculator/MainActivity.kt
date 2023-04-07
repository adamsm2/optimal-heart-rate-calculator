package com.example.optimalheartratecalculator

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatDelegate
import com.example.optimalheartratecalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        /*var sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE)
        var nightMode = sharedPreferences.getBoolean("night", false)
        if(nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }*/

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.FromMainToSettingsActivityButton.setOnClickListener{
            val Intent = Intent(this, SettingsActivity::class.java)
            startActivity(Intent)
        }

        var tvAge = binding.Age
        val numberPicker = binding.NumberPicker

        numberPicker.minValue = 1
        numberPicker.maxValue = 10
        tvAge.text = String.format("Your number is ${numberPicker.value}")

        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            tvAge.text = "Your number is $newVal"
        }



    }

    /*private fun loadSharedPreferences() {
        sharedPreferences = getSg
    }

     */
}