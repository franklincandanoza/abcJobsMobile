package com.uniandes.abcjobs.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.uniandes.abcjobs.databinding.ActivityMainBinding
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.uniandes.abcjobs.R
import org.intellij.lang.annotations.Language
import java.util.*
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var locale: Locale
    private var currentLanguage = "es"
    private var currentLang: String? = null
    companion object {
        public var dLocale: Locale? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentLanguage = intent.getStringExtra(currentLang).toString()

        var languageSp: Spinner =  findViewById(R.id.spinnerLanguage)
        languageSp?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> setLocale("es")
                    1 -> setLocale("en")
                }
                //println("********************************************")
                //println("Opcion escogida: $position")

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, CreateCandidateActivity::class.java)
            startActivity(intent)
        }

    }
    private fun setLocale(localeName: String) {
        if (localeName != currentLanguage) {
            //println("Ingresando a canviar")
            locale = Locale(localeName)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(
                this,
                MainActivity::class.java
            )
            refresh.putExtra(currentLang, localeName)
            startActivity(refresh)
        } else {
            Toast.makeText(
                this@MainActivity, "Language, , already, , selected!", Toast.LENGTH_SHORT).show();
        }
    }

}