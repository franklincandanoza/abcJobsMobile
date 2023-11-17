package com.uniandes.abcjobs.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.uniandes.abcjobs.R
import com.uniandes.abcjobs.databinding.ActivitySearchCandidateBinding
import com.uniandes.abcjobs.models.*
import com.uniandes.abcjobs.viewmodels.CandidateViewModel
import com.uniandes.abcjobs.viewmodels.ProjectViewModel
import com.uniandes.abcjobs.viewmodels.TechnologyViewModel
import kotlinx.coroutines.launch
import java.util.*


class SearchCandidatesActivity : AppCompatActivity(){
    private lateinit var viewModel: ProjectViewModel
    private lateinit var viewModel2: TechnologyViewModel
    private lateinit var viewModel3: CandidateViewModel
    private lateinit var candidateRequest: CandidateRequestSearch
    private var projectList = listOf<ProjectResponse>()
    private var candidateList = listOf<CandidateResponseSearch>()
    private var listP = ArrayList<String>()
    private lateinit var projectsAdapter : ArrayAdapter<String>
    private lateinit var profilesAdapter : ArrayAdapter<String>
    val ERROR_MESSAGE = "ErrorOnResponse"
    private var projectId: Int =0
    private var handler= Handler(Looper.getMainLooper())

    // initialize variables
    var textView: TextView? = null
    lateinit var selectedTechnology: BooleanArray
    var technologyList = ArrayList<Int>()
    var technologyArray = arrayOf("Java", "C++", "Kotlin", "C", "Python", "Javascript")
    lateinit var selectedAbility: BooleanArray
    var abilityList = ArrayList<Int>()
    var abilityArray = arrayOf("Adaptacion", "Toma Decisiones", "Resiliencia", "Creatividad",
        "Colaboracion", "Trabajo Equipo","Programacion Web","Programacion movil","Proyectos")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySearchCandidateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        viewModel2 = ViewModelProvider(this).get(TechnologyViewModel::class.java)
        viewModel3 = ViewModelProvider(this).get(CandidateViewModel::class.java)

        projectsAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
        profilesAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)

        // initialize selected technology array

        getProjectsByCompany(projectsAdapter)
        getTechnologies()

        binding.spinnerProjects.adapter = projectsAdapter
        binding.spinnerProfile.adapter = profilesAdapter

        var searchCandidateButton: CardView = binding.SearchCandidateButton

        var cancelsearchCandidateButton: CardView = binding.cancelCreateCandidateButton

        binding.spinnerProjects?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                println("********************************************")
                val projectSelected = projectsAdapter.getItem(position)
                println("Opcion escogida: $position  - ${projectSelected.toString()}")
                val project_id = getProjectID(projectList,projectSelected.toString());
                projectId = project_id
                val response = getProfilesByProject(profilesAdapter, project_id)
                if(response.contains("ERROR"))
                    Toast.makeText(getApplicationContext(),getString(R.string.sinPerfiles),
                        Toast.LENGTH_LONG).show();
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("********************************************")
                println("No se escogio nada")
            }
        }

        selectedTechnology = BooleanArray(technologyArray.size)
        binding.technologiesList.setOnClickListener(View.OnClickListener { // Initialize alert dialog
            val builder = AlertDialog.Builder(this@SearchCandidatesActivity)
            // set title
            val title: String = getString(R.string.LabelTecnologias)
            builder.setTitle(title)
            // set dialog non cancelable
            builder.setCancelable(false)
            builder.setMultiChoiceItems(
                technologyArray, selectedTechnology
            ) { dialogInterface, i, b ->
                // check condition
                if (b) {
                    // when checkbox selected
                    // Add position in lang list
                    technologyList.add(i)
                    // Sort array list
                    Collections.sort(technologyList)
                } else {
                    // when checkbox unselected
                    // Remove position from langList
                    technologyList.remove(Integer.valueOf(i))
                }
            }
            builder.setPositiveButton(
                "OK"
            ) { dialogInterface, i -> // Initialize string builder
                val stringBuilder = StringBuilder()
                // use for loop
                for (j in technologyList.indices) {
                    // concat array value
                    stringBuilder.append(technologyArray[technologyList[j]])
                    // check condition
                    if (j != technologyList.size - 1) {
                        // When j value not equal
                        // to lang list size - 1
                        // add comma
                        stringBuilder.append(",")
                    }
                }
                // set text on textView
                binding.technologiesList.setText(stringBuilder.toString())
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialogInterface, i -> // dismiss dialog
                dialogInterface.dismiss()
            }
            builder.setNeutralButton(
                "Clear All"
            ) { dialogInterface, i ->
                // use for loop
                for (j in selectedTechnology.indices) {
                    // remove all selection
                    selectedTechnology[j] = false
                    // clear language list
                    technologyList.clear()
                    // clear text view value
                    binding.technologiesList.setText("")
                }
            }
            // show dialog
            builder.show()
        })

        selectedAbility= BooleanArray(abilityArray.size)
        binding.abilitiesList.setOnClickListener(View.OnClickListener { // Initialize alert dialog
            val builder = AlertDialog.Builder(this@SearchCandidatesActivity)
            // set title
            val title: String = getString(R.string.LabelTecnologias)
            builder.setTitle(title)
            // set dialog non cancelable
            builder.setCancelable(false)
            builder.setMultiChoiceItems(
                abilityArray, selectedAbility
            ) { dialogInterface, i, b ->
                // check condition
                if (b) {
                    // when checkbox selected
                    // Add position in lang list
                    abilityList.add(i)
                    // Sort array list
                    Collections.sort(abilityList)
                } else {
                    // when checkbox unselected
                    // Remove position from langList
                    abilityList.remove(Integer.valueOf(i))
                }
            }
            builder.setPositiveButton(
                "OK"
            ) { dialogInterface, i -> // Initialize string builder
                val stringBuilder = StringBuilder()
                // use for loop
                for (j in abilityList.indices) {
                    // concat array value
                    stringBuilder.append(abilityArray[abilityList[j]])
                    // check condition
                    if (j != abilityList.size - 1) {
                        // When j value not equal
                        // to lang list size - 1
                        // add comma
                        stringBuilder.append(",")
                    }
                }
                // set text on textView
                binding.abilitiesList.setText(stringBuilder.toString())
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialogInterface, i -> // dismiss dialog
                dialogInterface.dismiss()
            }
            builder.setNeutralButton(
                "Clear All"
            ) { dialogInterface, i ->
                // use for loop
                for (j in selectedAbility.indices) {
                    // remove all selection
                    selectedAbility[j] = false
                    // clear language list
                    abilityList.clear()
                    // clear text view value
                    binding.abilitiesList.setText("")
                }
            }
            // show dialog
            builder.show()
        })

        searchCandidateButton.setOnClickListener {

            var projectSpinner: Spinner = findViewById(R.id.spinnerProjects)

            if(projectSpinner == null || projectSpinner.selectedItem == null)
            {
                Toast.makeText(getApplicationContext(),getString(R.string.proyectoInvalido),
                    Toast.LENGTH_LONG).show();
                return@setOnClickListener
            }
            var profileSpinner: Spinner = findViewById(R.id.spinnerProfile)

            if(profileSpinner == null || profileSpinner.selectedItem == null)
            {
                Toast.makeText(getApplicationContext(),getString(R.string.perfilInvalido),
                    Toast.LENGTH_LONG).show();
                return@setOnClickListener
            }
            var projectName = projectSpinner.selectedItem.toString()
            var profileId = profileSpinner.selectedItem.toString()

            var technologies= binding.technologiesList.text.toString()
            var abilities= binding.abilitiesList.text.toString()
            var roleSpinner: Spinner = findViewById(R.id.roleFilter)
            var roleFilter = roleSpinner.selectedItem.toString()
            if(roleFilter.contains("Contains") ||  roleFilter.contains("Contiene"))
                roleFilter="contains"
            if(roleFilter.contains("Starts with") || roleFilter.contains("Comienza"))
                roleFilter="starts"
            if(roleFilter.contains("Equals to") || roleFilter.contains("Igual"))
                roleFilter="equal"

            var roleEditText: EditText = findViewById(R.id.roleName)
            var roleName = roleEditText.text.toString()
            var expRoleEditText: EditText = findViewById(R.id.roleYears)
            var roleYears = expRoleEditText.text.toString()

            var titleSpinner: Spinner = findViewById(R.id.titleFilter)
            var titleFilter = titleSpinner.selectedItem.toString()
            if(titleFilter.contains("Contains") ||  titleFilter.contains("Contiene"))
                titleFilter="contains"
            if(titleFilter.contains("Starts with") || titleFilter.contains("Comienza"))
                titleFilter="starts"
            if(titleFilter.contains("Equals to") || titleFilter.contains("Igual"))
                titleFilter="equal"
            var titleEditText: EditText = findViewById(R.id.titleName)
            var titleName = titleEditText.text.toString()
            var expTitleEditText: EditText = findViewById(R.id.titleYears)
            var titleYears = expTitleEditText.text.toString()

            println("************* Info request $technologies , $abilities, $roleFilter, $roleName, $roleYears")
            println("************* Info request $titleFilter , $titleName, $titleYears")
            var searchRequest="$roleFilter&role=$roleName&roleExperience=$roleYears&technologies=$technologies&abilities=$abilities&titleFilter=$titleFilter&title=$titleName&titleExperience=$titleYears"


            var candidateRequest = CandidateRequestSearch(
                roleFilter,
                roleName,
                roleYears,
                technologies,
                abilities,
                titleFilter,
                titleName,
                titleYears
            )
            //searchCandidates(candidateRequest)

            val intent = Intent(this, ResultSearchActivity::class.java).also {

                it.putExtra("projectId", projectId.toString() )
                it.putExtra("projectName", projectName.toString() )
                it.putExtra("profileId", profileId.toString() )
                it.putExtra("roleFilter", roleFilter.toString() )
                it.putExtra("roleName", roleName.toString() )
                it.putExtra("roleYears", roleYears.toString() )
                it.putExtra("technologies", technologies.toString() )
                it.putExtra("abilities", abilities.toString() )
                it.putExtra("titleFilter", titleFilter.toString() )
                it.putExtra("titleName", titleName.toString() )
                it.putExtra("titleYears", titleYears.toString() )
                startActivity(it)
            }

        }

    }



    private fun createObservers(){
        viewModel.eventNetworkError.observe(this, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

        viewModel.eventLoginFail.observe(this, Observer<Boolean> { isLoginError ->
            if (isLoginError) onLoginFail()
        })

        viewModel.eventLoginFail.observe(this, Observer<Boolean> { isLoginError ->
            if (isLoginError) onLoginFail()
        })
    }

    private fun getProjectsByCompany(projectsAdapter: ArrayAdapter<String>) {
        var projects = listOf<ProjectResponse>()
        var list_projects = ArrayList<String>()
        lifecycleScope.launch {
            viewModel.getProjectsbyCompany({
                //listP.add(R.string.invalidSelect.toString())
                projectList = it
                println("Tamaño de projectList: ${projectList.size}")
                projects = it
                if(projects.size > 0) {
                    for (project in projects) {
                        println("Project id: ${project.id} Name: " + project.project_name.toString())
                        list_projects.add(project.project_name.toString())
                    }
                    projectsAdapter.addAll(list_projects)
                }
                it.size?.let { it1 -> Log.d("Success", it1.toString()) }
            },
                {
                    Log.d("Error", it.toString())
                })
        }

    }

    private fun getProfilesByProject(profilesAdapter: ArrayAdapter<String>, project_id: Int):String {
        var profiles = listOf<ProfileResponse>()
        var message=""
        var exception_msg=""
        var list_profiles = ArrayList<String>()
        lifecycleScope.launch {
            viewModel.getProfilesbyProject(project_id ,{
                //listP.add(R.string.invalidSelect.toString())
                profiles = it
                if(profiles.size > 0) {
                    for (profile in profiles) {
                        println("Profile: ${profile.name} ")
                        list_profiles.add(profile.name.toString())
                    }

                }
                profilesAdapter.addAll(list_profiles)
                it.size?.let { it1 -> Log.d("Success", it1.toString()) }
                message="OK,List complete"
            },
                {
                    Log.d("Error", it.toString())
                    exception_msg=processException(it, "Profile(s)")
                    message="ERROR,${exception_msg}"
                })
        }
        println("*******************++ $message")
        return message
    }

    private fun getTechnologies() {
        var technologies : List<TechnologyResponse>
        var list_profiles = arrayOf<String>()
        lifecycleScope.launch {
            viewModel2.getTechnologies({
                //listP.add(R.string.invalidSelect.toString())
                technologies = it
                if(technologies.size > 0) {
                    technologyArray = arrayOf()
                    for (technology in technologies) {
                        println("Technology: ${technology.name} ")
                        list_profiles=addElement(list_profiles, technology.name.toString())
                    }
                    technologyArray=list_profiles
                    selectedTechnology = BooleanArray(technologyArray.size)
                }
                it.size?.let { it1 -> Log.d("Success", it1.toString()) }
            },
                {
                    Log.d("Error", it.toString())

                })
        }

    }

    private fun searchCandidates(search_request: CandidateRequestSearch) {
        var candidates : List<CandidateResponseSearch>
        var list_candidates = ArrayList<String>()
        lifecycleScope.launch {
            viewModel3.searchCandidate(search_request, {
                //listP.add(R.string.invalidSelect.toString())
                candidateList = it
                println("Tamaño de candidateList: ${candidateList.size}")
                candidates = it
                if(candidates.size > 0) {
                    for (candidate in candidates) {
                        println("Person id: ${candidate.person_id} Name: ${candidate.first_name} ${candidate.last_name} age: ${candidate.age} " +
                                "roles: ${candidate.roles} tecnologias: ${candidate.technologies} titulos ${candidate.titles} puntaje: ${candidate.score}" )
                    }
                    //projectsAdapter.addAll(list_projects)
                }
                it.size?.let { it1 -> Log.d("Success", it1.toString()) }
            },
                {
                    Log.d("Error", it.toString())
                })
        }

    }

    private fun onLoginFail() {
        Log.i("onLoginFailProjectsInfo", ""+!viewModel.isUnSuccessShownProjectsInfo.value!!)
        if(!viewModel.isUnSuccessShownProjectsInfo.value!!) {
            Toast.makeText(applicationContext, resources.getString(R.string.sesionCerrada), Toast.LENGTH_LONG).show()
            viewModel.onUnSuccessShownProjectsInfo()
        }
    }

    private fun onNetworkError() {
        Log.i("onNetworkError", ""+!viewModel.isNetworkErrorShown.value!!)
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(applicationContext, resources.getString(R.string.networkError), Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    private fun getProjectID(projects: List<ProjectResponse>,projectSelected: String): Int
    {
        println("Tamaño de projects: ${projects.size}")
        var project_id=0
        for(project in projects)
            if(project.project_name.toString() === projectSelected) {
                println("Encontrado ID: ${project.id}")
                project_id = project.id.toInt()
            }
        return project_id
    }
    fun addElement(arr: Array<String>, element: String): Array<String> {
        val mutableArray = arr.toMutableList()
        mutableArray.add(element)
        return mutableArray.toTypedArray()
    }
    private fun processException(exception: Exception, origin: String): String {
        Log.d("Error", exception.toString())
        var message =""
        if (exception is retrofit2.HttpException) {
            val err = exception as retrofit2.HttpException
            //Log.d(ERROR_MESSAGE, err.response()?.code().toString())
            if (err.response()?.code() == 404) {
                message="$origin Not Found"
            } else {
                message="$origin response code " + err.response()?.code().toString()
            }
            Log.d(ERROR_MESSAGE, message)
        }
        return message
    }
    private fun printMessage(message: String )
    {
        handler?.postDelayed({
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG ).show();
        },1000)
    }
}


