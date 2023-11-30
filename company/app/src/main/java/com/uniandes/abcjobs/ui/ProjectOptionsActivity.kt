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
import com.uniandes.abcjobs.databinding.ActivityProjectOptionsBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ProjectOptionsActivity : AppCompatActivity() {
    //private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityProjectOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityProjectOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            val intent = Intent(this, CompanyOptionsActivity::class.java)
            startActivity(intent)
        }
        binding.botonBusqueda.setOnClickListener {
            val intent = Intent(this, SearchCandidatesActivity::class.java)
            startActivity(intent)
        }
        binding.cardPerformance.setOnClickListener {
            val intent = Intent(this, CreateEvaluationPerformanceActivity::class.java)
            startActivity(intent)
        }
        binding.backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}
