package com.uniandes.abcjobs.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.uniandes.abcjobs.R
import com.uniandes.abcjobs.databinding.ActivityListInterviewsBinding
import com.uniandes.abcjobs.databinding.ActivityRegisterTestResultBinding
import com.uniandes.abcjobs.models.Company
import com.uniandes.abcjobs.models.InterviewResult
import com.uniandes.abcjobs.models.Project
import com.uniandes.abcjobs.models.RegisterResultTestRequest
import com.uniandes.abcjobs.models.Test
import com.uniandes.abcjobs.viewmodels.CandidateViewModel
import com.uniandes.abcjobs.viewmodels.InterviewViewModel
import com.uniandes.abcjobs.viewmodels.RegisterTestResultViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class ListInterviewsActivity : AppCompatActivity(){

    private lateinit var viewModel: RegisterTestResultViewModel

    private lateinit var viewModel3: InterviewViewModel

    private lateinit var viewModel2: CandidateViewModel

    private lateinit var companies:List<Company>

    private lateinit var projects:List<Project>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityListInterviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(RegisterTestResultViewModel::class.java)

        viewModel2=ViewModelProvider(this).get(CandidateViewModel::class.java)

        viewModel3=ViewModelProvider(this).get(InterviewViewModel::class.java)

        viewModel3.companies.observe(this,{
            if (it!=null){
                companies=it
                var testSpinner: Spinner = findViewById(R.id.companies)
                val tests = it?.map { it.name+" ("+it.id+")" }?.toTypedArray()
                val arrayAdapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, tests)
                testSpinner.adapter = arrayAdapter
            }
        })

        viewModel3.projects.observe(this,{
            if (it!=null){
                projects=it
                var testSpinner: Spinner = findViewById(R.id.project)
                val tests = it?.map { it.name+" ("+it.id+")" }?.toTypedArray()
                val arrayAdapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, tests)
                testSpinner.adapter = arrayAdapter
            }
        })

        var companySpinner: Spinner = findViewById(R.id.companies)


        companySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                projects = emptyList()
                var companySpinner: Spinner = findViewById(R.id.companies)
                var company: String = companySpinner.selectedItem.toString()
                val regex = Regex("\\(([^)]+)\\)")
                val matchResult = regex.find(company)
                val id = matchResult?.groups?.get(1)?.value
                Log.i("Interview", "finding projects to company: "+id)

                if(id != null){
                    viewModel3.getProjectsByCompany(id)
                }else {
                    Log.i("Interview", "Vacío: "+id)
                    //
                }

            }
        }


        var projectSpinner: Spinner = findViewById(R.id.project)

        projectSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var projectSpinner: Spinner = findViewById(R.id.project)
                var project: String = projectSpinner.selectedItem.toString()
                val regex = Regex("\\(([^)]+)\\)")
                val matchResult = regex.find(project)
                val id = matchResult?.groups?.get(1)?.value

                Log.i("Interview", "finding interviews result to project: "+id)
                if(id != null){
                    getInterviews(id)
                }else {
                    Log.i("Interview", "Vacío: "+id)
                    //
                }
            }
        }


        viewModel3.getCompanies()

        var AcceptButton: CardView = binding.Accept

        AcceptButton.setOnClickListener {
            val refresh = Intent(
                this,
                CompanyOptionsActivity::class.java
            )
            startActivity(refresh)
        }

    }

    private fun getInterviews(projectId: String){
        var interviews = listOf<InterviewResult>()
        val container: LinearLayout = findViewById(R.id.inferior)

        for (i in container.childCount - 1 downTo 0) {
            val childView = container.getChildAt(i)
            if (childView is CardView) {
                container.removeViewAt(i)
            }
        }

        lifecycleScope.launch (Dispatchers.Main) {
            viewModel3.getResultsByProject(projectId, {
                //listP.add(R.string.invalidSelect.toString())

                interviews = it
                Log.i("Interview", "Tamaño de resultados: "+it)

                for (interview in interviews) {
                    Log.i("Interview", "" + interview.toString())

                    container.post {
                        var cardView = createResult(interview)

                        //val cardView = createInterview(interview)
                        container.addView(cardView)
                        container.addView(createSeparator())
                    }

                }

                it.size?.let { it1 -> Log.d("Success", it1.toString()) }
            },
                {
                    Log.d("Error", it.toString())
                })
        }

    }

    private fun createSeparator(): View {
        val separator = View(this)
        separator.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            15
        )
        separator.setBackgroundColor(resources.getColor(R.color.Gris00AOC)) // Puedes definir el color en colors.xml
        return separator
    }

    private fun createResult(result: InterviewResult): CardView {

        Log.i("Interview", "Creando card view para el candidato: "+result.fullname)
        // Inflar el layout de la CardView
        val cardView = layoutInflater.inflate(R.layout.result_card, null) as CardView

        val textViewCandidate: TextView = cardView.findViewById(R.id.candidate)
        val textViewProfile: TextView = cardView.findViewById(R.id.profile)
        val textViewQualification: TextView = cardView.findViewById(R.id.qualification)

        // Establecer los datos en los elementos de la CardView
        textViewCandidate.text = getString(R.string.CandidateNameLabelonInterview)+":"+ result.fullname
        textViewProfile.text = getString(R.string.ProfileNameLabelonInterview)+":"+ result.profile
        textViewQualification.text = getString(R.string.QualificationLabelonInterview)+":"+ result.qualification
        Log.i("Interview", "Creada card view para: "+result.profile)


        // Configurar eventos o realizar otras acciones según sea necesario

        return cardView
    }

    @SuppressLint("SuspiciousIndentation")
    private fun registerTest(registerTestRequest: RegisterResultTestRequest) {
        lifecycleScope.launch {
          var message:String =""
          val responseCode = viewModel.registerTestResult(registerTestRequest)
            if (responseCode.length == 0)
                message=resources.getString(R.string.testCreated)

            val toast =
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)

            toast.show()
        }
        val intent = Intent(this, CompanyOptionsActivity::class.java)
        startActivity(intent)

    }



    fun isValidNumber(num: Int?, min: Int, max: Int) : Boolean {
        num?.let {

            return num>=min && num<=max
        } ?: return false
    }

    fun isValidNumber(num: String?, min: Int?, max: Int?) : Boolean {
        num?.let {
            val numberPattern = "[0-9]{" + min.toString() + "," + max.toString() + "}$"
            val numberMatcher = Regex(numberPattern)

            return numberMatcher.find(num) != null
        } ?: return false
    }
    private fun openCancelDialog(intent: Intent){
        val builder = AlertDialog.Builder(this@ListInterviewsActivity)
        builder.setMessage(R.string.confirmCancel)
        builder.setTitle(R.string.warning)
        builder.setCancelable(false)
        builder.setPositiveButton(R.string.yes,
            DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                startActivity(intent)
            })

        builder.setNegativeButton(R.string.no,
            DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                dialog.cancel()
            } as DialogInterface.OnClickListener)

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun show_error(error:String) {
        lifecycleScope.launch {
            val toast =
                Toast.makeText(applicationContext, error, Toast.LENGTH_LONG)

            toast.show()
        }

    }
}
