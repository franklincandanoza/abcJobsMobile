package com.uniandes.abcjobs.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.uniandes.abcjobs.databinding.ActivityCompanyOptionsBinding


class CompanyOptionsActivity : AppCompatActivity() {
    //private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityCompanyOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCompanyOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
                finish()
            }


    }

    fun registerTest(view: View) {
        val refresh = Intent(
            this,
            RegisterTestResultActivity::class.java
        )
        startActivity(refresh)
    }

    fun viewInterviews(view: View) {
        val refresh = Intent(
            this,
            ListInterviewsActivity::class.java
        )
        startActivity(refresh)
    }
}