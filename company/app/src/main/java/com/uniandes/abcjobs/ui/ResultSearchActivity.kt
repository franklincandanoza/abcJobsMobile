package com.uniandes.abcjobs.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.uniandes.abcjobs.R
import com.uniandes.abcjobs.databinding.ActivityResultSearchBinding
import com.uniandes.abcjobs.models.CandidateRequestSearch
import com.uniandes.abcjobs.models.CandidateResponseSearch
import com.uniandes.abcjobs.models.ProjectMemberRequest
import com.uniandes.abcjobs.models.ProjectResponse
import com.uniandes.abcjobs.ui.adapters.CandidateSearchAdapter
import com.uniandes.abcjobs.viewmodels.CandidateViewModel
import com.uniandes.abcjobs.viewmodels.ProjectViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.*


@Serializable
data class Response(val label: String, val message: String)

class ResultSearchActivity : AppCompatActivity() {
    // initialize variables
    private lateinit var viewModel3: CandidateViewModel
    private lateinit var viewModel: ProjectViewModel
    private lateinit var searchAdapter: CandidateSearchAdapter
    private lateinit var candidateRequest: CandidateRequestSearch
    private var projectList = listOf<ProjectResponse>()
    private var candidateList = listOf<CandidateResponseSearch>()
    private var handler= Handler(Looper.getMainLooper())
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<*>
    //private val binding: ActivityExampleBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityResultSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //var recyclerView = binding.candidatesRv
        /*searchAdapter = CandidateSearchAdapter()
        recyclerView.adapter = searchAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)*/

        manager = LinearLayoutManager(this)

        viewModel3 = ViewModelProvider(this).get(CandidateViewModel::class.java)
        viewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)

        viewModel3.candidatesSearch.observe(this, Observer<List<CandidateResponseSearch>>{
            it.apply {
                Log.d("**** Observer", "Detectado cambios")
                //searchAdapter!!.candidates = this
            }
        })

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
       // searchCandidates(candidateRequest, binding, handler, recycler2, manager)
        searchCandidates(candidateRequest,manager, binding, projectId, profileId)
        binding.backButton.setOnClickListener {
            val intent = Intent(this, SearchCandidatesActivity::class.java)
            startActivity(intent)
        }

    }

    private fun searchCandidates(search_request: CandidateRequestSearch, manager: LayoutManager,
                                 binding: ActivityResultSearchBinding, project: String?, profile: String?) {
        var candidates : List<CandidateResponseSearch>
        lifecycleScope.launch {
            viewModel3.searchCandidate(search_request, {
                candidateList = it
                println("Tamaño de candidateList: ${candidateList.size}")
                candidates = it
                var i=0
                if(candidates.size > 0) {

                    handler.post {
                        println("Llenando el recicler ${candidates.size}")

                        val recyclerAdapter = CandidateSearchAdapter(candidates!!, project, profile,::addMember)
                        val recyclerView2 = binding.candidatesRv
                        recyclerView2.layoutManager = manager
                        recyclerView2.adapter = recyclerAdapter
                    }
                    for (candidate in candidates) {
                        println("Person id: ${candidate.person_id} Name: ${candidate.first_name} ${candidate.last_name} age: ${candidate.age} " +
                                "roles: ${candidate.roles} tecnologias: ${candidate.technologies} titulos ${candidate.titles} puntaje: ${candidate.score}" )
                        i++
                    }

                }
                else{
                    printMessage(getString(R.string.noExistenCandidatos).toString())
                }
                it.size.let { it1 -> Log.d("Success", it1.toString()) }
                candidateList = it
            },
                {
                    Log.d("Error", it.toString())
                    if(it.toString().contains("404"))
                        printMessage(getString(R.string.noExistenCandidatos).toString())
                })
        }
    }

    private fun addMember(request : ProjectMemberRequest)
    {

        lifecycleScope.launch {

            viewModel.addMember(request, {
                var messsage : String =""
                try {

                        val response = it.get("msg")
                        var message=""
                        if (response.toString().contains("Member has been")) {
                            message = getString(R.string.miembroCreado).toString()
                        } else {
                            message = response.toString()
                        }
                        printMessage(message)
                        println("Valor de msg ${response.toString()}")

                }
                catch (e:Exception){
                    Log.d("ERROR", "${e.message}")
                }

            },
                {
                    Log.d("Error", it.toString())
                })
        }
    }
    private fun printMessage(message: String )
    {
        handler?.postDelayed({
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG ).show();
        },1000)
    }
}
