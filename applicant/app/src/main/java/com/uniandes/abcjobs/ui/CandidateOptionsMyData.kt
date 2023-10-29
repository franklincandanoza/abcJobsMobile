package com.uniandes.abcjobs.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uniandes.abcjobs.databinding.ActivityCandidateMyDataBinding


class CandidateOptionsMyData : AppCompatActivity() {
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

    }


}