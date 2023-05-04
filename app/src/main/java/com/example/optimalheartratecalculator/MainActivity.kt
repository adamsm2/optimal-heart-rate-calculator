package com.example.optimalheartratecalculator

import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatDelegate
import com.example.optimalheartratecalculator.databinding.ActivityMainBinding
import com.example.optimalheartratecalculator.databinding.HelpWindowBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var currentLanguage : String
    private val maxAge = 100
    private val minAge = 1
    private var animationsON = true

    companion object {
        var age = 1
        private var maleIsSet: Boolean? = null
    }
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ContextWrapper(newBase.setAppLocale()))
    }

    private fun Context.setAppLocale() : Context {
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val language = sharedPreferences.getString("language", "en")
        currentLanguage = language.toString()
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return createConfigurationContext(config)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.Age.text = age.toString()
        setGender()
        loadSharedPreferences()
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        animationsON = sharedPreferences.getBoolean("animations", true)
        val language = sharedPreferences.getString("language", "en")
        if (language != currentLanguage) {
            recreate()
        }
    }

    override fun onPause() {
        super.onPause()
        val editor = sharedPreferences.edit()
        editor.putInt("age", age)
        editor.apply()
    }

    private fun loadSharedPreferences() {
        animationsON = sharedPreferences.getBoolean("animations", true)
        loadMode()
        loadAge()
    }
    private fun loadMode() {
        val nightMode = sharedPreferences.getBoolean("night", false)
        if(nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            AppCompatDelegate.getDefaultNightMode()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun loadAge() {
        age = sharedPreferences.getInt("age", 1)
        binding.Age.text = age.toString()
    }


    private fun setGender() {
        if(maleIsSet == true) {
            setMale()
        } else if (maleIsSet == false) {
            setFemale()
        }
    }

    private fun setMale() {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.MaleRelativeLayout.setBackgroundResource(R.drawable.night_mode_after_click)
            binding.FemaleRelativeLayout.setBackgroundResource(R.drawable.night_mode_backgrounds)
        } else {
            binding.MaleRelativeLayout.setBackgroundResource(R.drawable.after_click)
            binding.FemaleRelativeLayout.setBackgroundResource(R.drawable.backgrounds)
        }
    }

    private fun setFemale() {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.FemaleRelativeLayout.setBackgroundResource(R.drawable.night_mode_after_click)
            binding.MaleRelativeLayout.setBackgroundResource(R.drawable.night_mode_backgrounds)
        } else {
            binding.FemaleRelativeLayout.setBackgroundResource(R.drawable.after_click)
            binding.MaleRelativeLayout.setBackgroundResource(R.drawable.backgrounds)
        }
    }

    private fun unsetGender() {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.FemaleRelativeLayout.setBackgroundResource(R.drawable.night_mode_backgrounds)
            binding.MaleRelativeLayout.setBackgroundResource(R.drawable.night_mode_backgrounds)
        } else {
            binding.FemaleRelativeLayout.setBackgroundResource(R.drawable.backgrounds)
            binding.MaleRelativeLayout.setBackgroundResource(R.drawable.backgrounds)
        }
    }

    private fun showHelpWindow() {
        val helpWindowDialog = Dialog(this)
        val helpWindowBinding = HelpWindowBinding.inflate(layoutInflater)
        helpWindowDialog.setContentView(helpWindowBinding.root)
        helpWindowDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        helpWindowDialog.show()

        helpWindowBinding.CloseWindowButton.setOnClickListener{
            helpWindowDialog.dismiss()
        }

    }

    private fun showResult() {
        if(maleIsSet == null) {
            binding.CalculationResult.text = getString(R.string.set_gender)
            return
        }
        binding.CalculationResult.text = if (maleIsSet as Boolean) "%.1f".format(0.5 * (226 - age)) + " - " + "%.1f".format(0.85 * (226 - age))  + "\n" + getString(R.string.BPM) else "%.1f".format(0.5 * (220 - age)) + " - " + "%.1f".format(0.85 * (220 - age))  + "\n" + getString(R.string.BPM)
        if(animationsON) {
            binding.CalculationResult.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.shake))
        }
    }

    private fun setListeners() {
        binding.MaleRelativeLayout.setOnClickListener{
            if(maleIsSet == true) {
                maleIsSet = null
                unsetGender()
                binding.CalculationResult.text = getString(R.string.set_gender)
                return@setOnClickListener
            }
            maleIsSet = true
            setMale()
            showResult()
        }

        binding.FemaleRelativeLayout.setOnClickListener{
            if(maleIsSet == false) {
                maleIsSet = null
                unsetGender()
                binding.CalculationResult.text = getString(R.string.set_gender)
                return@setOnClickListener
            }
            maleIsSet = false
            setFemale()
            showResult()
        }

        binding.CalculateButton.setOnClickListener{
            showResult()
        }

        binding.FromMainToSettingsActivityButton.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.Plus.setOnClickListener{
            if(age < maxAge) {
                age++
                binding.Age.text = age.toString()
                showResult()
            }
        }

        binding.Plus.setOnLongClickListener {
            Thread {
                while(binding.Plus.isPressed) {
                    if(age < maxAge) {
                        age++
                        binding.Age.text = age.toString()
                        showResult()
                    }
                    Thread.sleep(100)
                }
            }.start()
            true
        }

        binding.Plus.setOnTouchListener {_, event ->
            false
        }

        binding.Minus.setOnClickListener{
            if(age > minAge) {
                age--
                binding.Age.text = age.toString()
                showResult()
            }
        }

        binding.Minus.setOnLongClickListener {
            Thread {
                while(binding.Minus.isPressed) {
                    if(age > minAge) {
                        age--
                        binding.Age.text = age.toString()
                        showResult()
                    }
                    Thread.sleep(100)
                }
            }.start()
            true
        }

        binding.Minus.setOnTouchListener {_, event ->
            false
        }

        binding.HelpButton.setOnClickListener{
            showHelpWindow()
        }

    }
}