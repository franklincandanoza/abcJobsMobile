package com.uniandes.abcjobs.ui

import android.content.DialogInterface
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.uniandes.abcjobs.R
import com.uniandes.abcjobs.databinding.ActivityCreatePerformanceEvaluationBinding
import com.uniandes.abcjobs.databinding.ActivityResultSearchBinding
import com.uniandes.abcjobs.models.PerformanceEvaluationRequest
import com.uniandes.abcjobs.models.*
import com.uniandes.abcjobs.ui.adapters.CandidateSearchAdapter
import com.uniandes.abcjobs.viewmodels.CandidateViewModel
import com.uniandes.abcjobs.viewmodels.PerformanceEvaluationViewModel
import com.uniandes.abcjobs.viewmodels.ProjectViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.*
import java.util.*

class CreateEvaluationPerformanceActivity : AppCompatActivity(){
    private lateinit var viewModel: ProjectViewModel
    private lateinit var viewModel2: PerformanceEvaluationViewModel
    private var projectList = listOf<ProjectResponse>()
    private var memberData =  arrayOf("0","0")
    private lateinit var projectsAdapter : ArrayAdapter<String>
    private lateinit var membersAdapter: ArrayAdapter<String>

    private lateinit var memberResponse: ProjectMemberResponse
    private var memberList = listOf<ProjectMemberResponse>()
    private var projectId: Int =0
    private var handler= Handler(Looper.getMainLooper())
    val ERROR_MESSAGE = "ErrorOnResponse"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreatePerformanceEvaluationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        viewModel2 = ViewModelProvider(this).get(PerformanceEvaluationViewModel::class.java)


        projectsAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
        membersAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)

        // initialize selected technology array

        getProjectsByCompany(projectsAdapter)
        //getTechnologies()

        binding.spinnerProjects.adapter = projectsAdapter
        binding.spinnerMembers.adapter = membersAdapter

        var createEvaluationButton: CardView = binding.createEvaluationButton

        var cancelEvaluationButton: CardView = binding.cancelCreateEvaluationButton

        binding.spinnerProjects?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                println("********************************************")
                val projectSelected = projectsAdapter.getItem(position)
                println("Opcion escogida: $position  - ${projectSelected.toString()}")
                val project_id = getProjectID(projectList,projectSelected.toString());
                projectId = project_id
                val response = getMembersByProject(membersAdapter, project_id)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("********************************************")
                println("No se escogio nada")
            }
        }
        binding.spinnerMembers?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                println("********************************************")
                val memberSelected = membersAdapter.getItem(position)
                println("Opcion escogida: $position  - ${memberSelected.toString()}")
                val member_data = getMemberData(memberList,memberSelected.toString());
                memberData = member_data
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("********************************************")
                println("No se escogio nada en miembros")
            }
        }

        createEvaluationButton.setOnClickListener{
            var projectSpinner: Spinner = findViewById(R.id.spinnerProjects)

            if(projectSpinner == null || projectSpinner.selectedItem == null)
            {
                Toast.makeText(getApplicationContext(),getString(R.string.proyectoInvalido),
                    Toast.LENGTH_LONG).show();
                return@setOnClickListener
            }
            var memberSpinner: Spinner = findViewById(R.id.spinnerMembers)

            if(memberSpinner == null || memberSpinner.selectedItem == null)
            {
                Toast.makeText(getApplicationContext(),getString(R.string.miembroInvalido),
                    Toast.LENGTH_LONG).show();
                return@setOnClickListener
            }
            var scoreSpinner :  Spinner = findViewById(R.id.spinnerScore)
            if(scoreSpinner == null || scoreSpinner.selectedItem == null)
            {
                Toast.makeText(getApplicationContext(),getString(R.string.miembroInvalido),
                    Toast.LENGTH_LONG).show();
                return@setOnClickListener
            }
            var detailsEditText: EditText = findViewById(R.id.evaluationDetails)
            var evaluationDetail = detailsEditText.text.toString()

            if(evaluationDetail.isEmpty() || !isValidDetail(evaluationDetail)){
                detailsEditText.error = resources.getString(R.string.observacionInvalida)
                return@setOnClickListener
            }
            var score = scoreSpinner.selectedItem.toString()

            var evaluationRequest  = PerformanceEvaluationRequest(
                score,
                evaluationDetail,
                projectId.toString(),
                memberData[0].toString(),
                memberData[1].toString()
            )

            println("Enviando valores ${score} - ${evaluationDetail} - ${projectId} -${memberData[0]} - ${memberData[1]}")
            createEvaluation(evaluationRequest)

        }
        cancelEvaluationButton.setOnClickListener {
            openCancelDialog(Intent(this, ProjectOptionsActivity::class.java))
        }

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
    private fun getMembersByProject(membersAdapter: ArrayAdapter<String>, project_id: Int):String {
        var members = listOf<ProjectMemberResponse>()
        var message=""
        var exception_msg=""
        var list_members = ArrayList<String>()
        lifecycleScope.launch {
            viewModel.getMembersbyProject(project_id ,{
                //listP.add(R.string.invalidSelect.toString())
                memberList = it
                members = it
                if(members.size > 0) {
                    for (member in members) {
                        println("Profile: ${member.name} ")
                        list_members.add(member.name.toString())
                    }

                }
                membersAdapter.addAll(list_members)
                it.size?.let { it1 -> Log.d("Success", it1.toString()) }
                message="OK,List complete"
            },
                {
                    //Log.d("Error", it.toString())
                    println("Exception::: ${it.toString()}")
                    exception_msg=processException(it, "Members(s)")
                    message="ERROR,${exception_msg}"
                    //printMessage(it.toString())
                    if(it.toString().contains("404")) {
                        printMessage(getString(R.string.sinMiembros))
                        handler?.postDelayed({
                            membersAdapter.clear()
                        }, 200)
                    }
                })
        }
        println("*******************++ $message")
        return message
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
    private fun getMemberData(members: List<ProjectMemberResponse>,memberSelected: String): Array<String>
    {
        println("Tamaño de projects: ${members.size}")
        var dataArray = arrayOf("0","0")
        for(member in members)
            if(member.name.toString() === memberSelected) {
                println("Encontrado person : ${member.person_id}")
                println("Encontrado ID: ${member.id}")
                dataArray[0] = member.person_id.toString()
                dataArray[1] = member.id.toString()
            }
        return dataArray
    }

    private fun createEvaluation(request : PerformanceEvaluationRequest)
    {

        lifecycleScope.launch {
            viewModel2.createEvaluation(request, {
                var messsage : String =""
                try {
                        val response = it.get("msg")
                        var message=""
                        if (response.toString().contains("Performance evaluation has been")) {
                            message = getString(R.string.evaluacionCreada).toString()
                                } else {
                                    message = response.toString()
                                }
                    printMessage(message)
                    println("Valor de msg ${response.toString()}")
                    handler?.postDelayed({
                        val intent = Intent(getApplicationContext(), ProjectOptionsActivity::class.java)
                        startActivity(intent)
                    }, 1500)
                }
                catch (e:Exception){
                    Log.d("ERROR", "${e.message}")
                    println("Entradno al catch")
                }

            },
                {
                    Log.d("Error", it.toString())
                    if (it.toString().contains("400")) {
                        val message = getString(R.string.evaluacionExistente).toString()
                        printMessage(message)
                    }
                })
        }
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
    fun isValidDetail(detail: String?) : Boolean {
        detail?.let {
            val detailPattern = "(?=^.{10,60}$)(?=.*[A-Z]|[a-z]).*$"
            val detailMatcher = Regex(detailPattern)

            return detailMatcher.find(detail) != null
        } ?: return false
    }
    private fun openCancelDialog(intent: Intent){
        val builder = android.app.AlertDialog.Builder(this@CreateEvaluationPerformanceActivity)
        builder.setMessage(getString(R.string.preguntaEvaluacion).toString())
        builder.setTitle(getString(R.string.advertencia).toString())
        builder.setCancelable(false)
        builder.setPositiveButton(getString(R.string.si).toString(),
            DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                startActivity(intent)
            })

        builder.setNegativeButton(getString(R.string.no).toString(),
            DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                dialog.cancel()
            } as DialogInterface.OnClickListener)

        val alertDialog = builder.create()
        alertDialog.show()
    }
}