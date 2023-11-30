package com.uniandes.abcjobs.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uniandes.abcjobs.databinding.ActivityCandidateMyDataBinding


class CandidateOptionsMyDataActivity : AppCompatActivity() {
    //private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityCandidateMyDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCandidateMyDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createAcademicInfo.setOnClickListener {
            val intent = Intent(this, CreateCandidateAcademicInfoActivity::class.java)
            startActivity(intent)
        }

        binding.createTechnicalInfo.setOnClickListener {
            val intent = Intent(this, CandidateOptionsTechnicalInfo::class.java)
            startActivity(intent)
        }

        binding.createWorkingInfo.setOnClickListener {
            val intent = Intent(this, CreateCandidateWorkingInfoActivity::class.java)
            startActivity(intent)
        }
        binding.backButton.setOnClickListener{
            val intent = Intent(this, CandidateOptionsActivity::class.java)
            startActivity(intent)
        }

    }


}