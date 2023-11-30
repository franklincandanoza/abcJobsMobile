package com.uniandes.abcjobs.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uniandes.abcjobs.databinding.ActivityCandidateMyDataBinding
import com.uniandes.abcjobs.databinding.ActivityCandidateTecnicalInfoBinding


class CandidateOptionsTechnicalInfo : AppCompatActivity() {
    //private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityCandidateTecnicalInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCandidateTecnicalInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createTechnicalRoles.setOnClickListener {
            val intent = Intent(this, CreateCandidateTechnicalRolesInfoActivity::class.java)
            startActivity(intent)
        }

        binding.createTechnologies.setOnClickListener {
            val intent = Intent(this, CreateCandidateTechnologyInfoActivity::class.java)
            startActivity(intent)
        }
        binding.backButton.setOnClickListener{
            val intent = Intent(this, CandidateOptionsMyDataActivity::class.java)
            startActivity(intent)
        }
    }


}