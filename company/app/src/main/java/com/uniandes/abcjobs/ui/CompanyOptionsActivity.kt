package com.uniandes.abcjobs.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.uniandes.abcjobs.R
import com.uniandes.abcjobs.databinding.ActivityCompanyOptionsBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CompanyOptionsActivity : AppCompatActivity() {
    //private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityCompanyOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCompanyOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardCrearProyecto.setOnClickListener {
            val intent = Intent(this, ProjectOptionsActivity::class.java)
            startActivity(intent)
        }
        binding.cardPerformance.setOnClickListener {
            val intent = Intent(this, MultiSpinner::class.java)
            startActivity(intent)
        }
        binding.cardBasicos.setOnClickListener {
            val intent = Intent(this, ExampleActivity::class.java).also {
                it.putExtra("Search_result", "1,2,3,5")
                startActivity(it)
            }
        }
    }
}