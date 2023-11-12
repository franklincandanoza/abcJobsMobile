package com.uniandes.abcjobs.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.uniandes.abcjobs.databinding.ActivityExampleBinding
import com.uniandes.abcjobs.models.CandidateRequestSearch
import com.uniandes.abcjobs.models.CandidateResponseSearch
import com.uniandes.abcjobs.models.ProjectResponse
import com.uniandes.abcjobs.viewmodels.CandidateViewModel
import kotlinx.coroutines.launch
import java.util.*


class ExampleActivity : AppCompatActivity() {
    // initialize variables
    private lateinit var viewModel3: CandidateViewModel
    private lateinit var candidateRequest: CandidateRequestSearch
    private var projectList = listOf<ProjectResponse>()
    private var candidateList = listOf<CandidateResponseSearch>()
    private var listP = ArrayList<String>()
    private var handler= Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel3 = ViewModelProvider(this).get(CandidateViewModel::class.java)


        val projectId = intent.getStringExtra("projectId")
        val projectName = intent.getStringExtra("projectName")
        val profileId = intent.getStringExtra("profileId")
        val roleFilter = intent.getStringExtra("roleFilter")
        val roleName = intent.getStringExtra("roleName")
        val roleYears = intent.getStringExtra("roleYears")
        val technologies = intent.getStringExtra("technologies")
        val abilities = intent.getStringExtra("abilities")
        val titleFilter = intent.getStringExtra("titleFilter")
        val titleName = intent.getStringExtra("titleName")
        val titleYears = intent.getStringExtra("titleYears")
        
        println("Recibido : proyect=$projectId&profile=$profileId$$roleFilter&role=$roleName&roleExperience=$roleYears&technologies=$technologies&abilities=$abilities&titleFilter=$titleFilter&title=$titleName&titleExperience=$titleYears")

        binding.nameProject.text = projectName
        binding.nameProfile.text = profileId

        var candidateRequest = CandidateRequestSearch(
            roleFilter.toString(),
            roleName.toString(),
            roleYears.toString(),
            technologies.toString(),
            abilities.toString(),
            titleFilter.toString(),
            titleName.toString(),
            titleYears.toString()
        )
        searchCandidates(candidateRequest, binding, handler)
        //binding.scrollSuperior.
        //println("************* Num candidates $numCandidates")

        /*var i=0

        for(candidate in candidateList)
        {
            i++;
            println("Valor de i ${i}")
            if(i==1)
            {
                binding.nameCandidate1.setText("${candidate.first_name} ${candidate.last_name}")
            }
            if(i==2)
            {
                binding.nameCandidate2.setText("${candidate.first_name} ${candidate.last_name}")
            }
            if(i==3) {
                binding.nameCandidate3.setText("${candidate.first_name} ${candidate.last_name}")
            }
        }*/
        //android:layout_marginStart="10dp"

        binding.backButton.setOnClickListener {
            val intent = Intent(this, SearchCandidatesActivity::class.java)
            startActivity(intent)
        }

    }

    private fun searchCandidates(search_request: CandidateRequestSearch, binding: ActivityExampleBinding, handle: Handler) {
        var candidates : List<CandidateResponseSearch>
        var numCandidates =0
        var list_candidates = ArrayList<String>()
        lifecycleScope.launch {
            viewModel3.searchCandidate(search_request, {
                //listP.add(R.string.invalidSelect.toString())
                candidateList = it
                println("Tamaño de candidateList: ${candidateList.size}")
                candidates = it
                var i=0
                if(candidates.size > 0) {
                    for (candidate in candidates) {
                        println("Person id: ${candidate.person_id} Name: ${candidate.first_name} ${candidate.last_name} age: ${candidate.age} " +
                                "roles: ${candidate.roles} tecnologias: ${candidate.technologies} titulos ${candidate.titles} puntaje: ${candidate.score}" )
                        i++
                    }
                    handler.post {
                        i=0
                        for (candidate in candidates) {
                            i++
                            println("Valor de i ${i}")
                            if(i==1)
                            {
                                binding.nameCandidate1.text = "${candidate.first_name} ${candidate.last_name}"
                                binding.academic1.text = "${candidate.titles}"
                                binding.score1.text = "${candidate.roles}"
                            }
                            if(i==2)
                            {
                                binding.nameCandidate2.text = "${candidate.first_name} ${candidate.last_name}"
                                binding.academic2.text = "${candidate.titles}"
                                binding.score2.text = "${candidate.roles}"
                            }
                            if(i==3) {
                                binding.nameCandidate3.text = "${candidate.first_name} ${candidate.last_name}"
                                binding.academic3.text = "${candidate.titles}"
                                binding.score3.text = "${candidate.roles}"
                            }
                        }
                    }
                    //projectsAdapter.addAll(list_projects)
                }
                it.size.let { it1 -> Log.d("Success", it1.toString()) }
                candidateList = it
            },
                {
                    Log.d("Error", it.toString())
                })
        }
    }
}
