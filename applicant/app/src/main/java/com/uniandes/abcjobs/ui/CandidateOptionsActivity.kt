package com.uniandes.abcjobs.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.uniandes.abcjobs.databinding.ActivityCandidateOptionsBinding


class CandidateOptionsActivity : AppCompatActivity() {
    //private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityCandidateOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCandidateOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun goMyData(view: View) {
        val intent = Intent(this, CandidateOptionsMyData::class.java)
        startActivity(intent)
    }
}