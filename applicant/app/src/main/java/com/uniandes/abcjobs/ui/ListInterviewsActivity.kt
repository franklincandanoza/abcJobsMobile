package com.uniandes.abcjobs.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Observer
import com.uniandes.abcjobs.R
import com.uniandes.abcjobs.databinding.ActivityCreateCandidateTechnicalRolesBinding
import com.uniandes.abcjobs.databinding.ActivityListInterviewsBinding
import com.uniandes.abcjobs.viewmodels.CandidateViewModel
import com.uniandes.abcjobs.models.CreateCandidateTechnicalRoleInfoRequest
import com.uniandes.abcjobs.models.Interview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.ArrayList

class ListInterviewsActivity : AppCompatActivity() {

    private lateinit var viewModel: CandidateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityListInterviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(CandidateViewModel::class.java)

        getMyInterviews()

        binding.backButton.setOnClickListener {

            val intent = Intent(this, CandidateOptionsActivity::class.java)
            startActivity(intent)
        }



    }

    private fun getMyInterviews() {


        var interviews = listOf<Interview>()
        val container: LinearLayout = findViewById(R.id.inferior)
        lifecycleScope.launch (Dispatchers.Main) {
            viewModel.refreshInterviews({
                //listP.add(R.string.invalidSelect.toString())

                interviews = it
                Log.i("Interview", "Tamaño de entrevistas: "+it)

                for (interview in interviews) {
                    Log.i("Interview", "" + interview.toString())

                    container.post {
                        var cardView = createInterview(interview)

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

    private fun createInterview(interview: Interview): CardView {

        Log.i("Interview", "Creando card view para: "+interview.profile)
        // Inflar el layout de la CardView
        val cardView = layoutInflater.inflate(R.layout.interview_card, null) as CardView

        val textViewTitle: TextView = cardView.findViewById(R.id.profile)
        val textViewDescription: TextView = cardView.findViewById(R.id.hour)

        Log.i("Interview", "Status: "+interview.status)
        if (interview.status == "SCHEDULED"){
            cardView.setCardBackgroundColor(resources.getColor(R.color.azulClaAOC))
            textViewTitle.setTextColor(resources.getColor(R.color.negroAOC))
        }
        else {
            cardView.setCardBackgroundColor(resources.getColor(R.color.Gris00AOC))
            textViewTitle.setTextColor(resources.getColor(R.color.negroAOC))
        }

        // Obtener referencias a los elementos dentro de la CardView


        // Establecer los datos en los elementos de la CardView
        textViewTitle.text = interview.profile
        textViewDescription.text = interview.where
        Log.i("Interview", "Creada card view para: "+interview.profile)


        // Configurar eventos o realizar otras acciones según sea necesario

        return cardView
    }

    private fun createObservers(){
        viewModel.eventNetworkError.observe(this, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

        viewModel.eventLoginFail.observe(this, Observer<Boolean> { isLoginError ->
            if (isLoginError) onLoginFail()
        })
    }

    private fun onNetworkError() {
        Log.i("onNetworkError", ""+!viewModel.isNetworkErrorShown.value!!)
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(applicationContext, resources.getString(R.string.networkError), Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    private fun onLoginFail() {
        Log.i("onLoginFailAcademicInfo", ""+!viewModel.isUnSuccessShownToCreateAcademicInfo.value!!)
        if(!viewModel.isUnSuccessShownToCreateAcademicInfo.value!!) {
            Toast.makeText(applicationContext, resources.getString(R.string.sesionCerrada), Toast.LENGTH_LONG).show()
            viewModel.onUnSuccessLoginShownToCreateAcademicInfo()
        }
    }
}