package com.uniandes.abcjobs.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uniandes.abcjobs.databinding.


class CompanyOptionsActivity : AppCompatActivity() {
    //private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityCompanyOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCompanyOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}