package com.uniandes.abcjobs.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Observer
import com.uniandes.abcjobs.R
import com.uniandes.abcjobs.databinding.ActivityCreateCandidateAcademicInfoBinding
import com.uniandes.abcjobs.models.CreateAcademicInfoRequest
import com.uniandes.abcjobs.viewmodels.CandidateViewModel
import kotlinx.coroutines.launch


class CreateCandidateAcademicInfoActivity : AppCompatActivity(){

    private lateinit var viewModel: CandidateViewModel
    /*private var canadidateAdapter: CandidateAdapter? = null*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityCreateCandidateAcademicInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //candidateAdapter = CandidateAdapter()
        viewModel = ViewModelProvider(this).get(CandidateViewModel::class.java)

        var createCandidateButton: CardView = binding.createCandidateAcademicInfoButton

        var cancelCreateCandidateButton: CardView = binding.cancelCreateCandidateAcademicInfoButton
        var vigenteCheckBox: CheckBox = binding.vigente

        var endMonthSpinner: Spinner = binding.endMonth
        var endYearSpinner: Spinner = binding.endYear

        vigenteCheckBox.setOnClickListener {

            endMonthSpinner.isEnabled = !vigenteCheckBox.isChecked
            endYearSpinner.isEnabled = !vigenteCheckBox.isChecked

        }

        createCandidateButton.setOnClickListener {

            // Do click handling here
            var titleEditText: EditText = findViewById(R.id.title)
            var candidateTitle = titleEditText.text.toString()

            var institutionEditText: EditText = findViewById(R.id.institution)
            var candidateInstitution = institutionEditText.text.toString()

            var countrySpinner: Spinner = findViewById(R.id.country)
            var candidateCountry = countrySpinner.selectedItem.toString().split("-").get(1).toString()

            var startMonthSpinner: Spinner = findViewById(R.id.startMonth)
            var candidateStartMonth = startMonthSpinner.selectedItem.toString().toInt()

            var startYearSpinner: Spinner = findViewById(R.id.startYear)
            var candidateStartYear = startYearSpinner.selectedItem.toString().toInt()

            var candidateEndMonth = endMonthSpinner.selectedItem.toString().toInt()

            var candidateEndYear = endYearSpinner.selectedItem.toString().toInt()

            var descriptionEditText: EditText = findViewById(R.id.description)
            var candidateDescription = descriptionEditText.text.toString()



            if(candidateTitle.isEmpty()){
                titleEditText.error = resources.getString(R.string.tituloInvalido)
                return@setOnClickListener
            }

            if(candidateInstitution.isEmpty()){
                institutionEditText.error = resources.getString(R.string.institucionInvalida)
                return@setOnClickListener
            }

            if(candidateDescription.isEmpty()){
                descriptionEditText.error = resources.getString(R.string.descriptionInvalida)
                return@setOnClickListener
            }

            var createAcademicInfoRequest = CreateAcademicInfoRequest(
                candidateTitle,
                candidateInstitution,
                candidateCountry,
                candidateStartMonth,
                candidateStartYear,
                if (vigenteCheckBox.isChecked) null else candidateEndMonth,
                if (vigenteCheckBox.isChecked) null else candidateEndYear,
                candidateDescription,
            )

            createCandidateAcademicInfo(createAcademicInfoRequest)

        }

        cancelCreateCandidateButton.setOnClickListener {
            openCancelDialog(Intent(this, MainActivity::class.java))
        }

        viewModel.eventNetworkError.observe(this, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

        viewModel.eventLoginFail.observe(this, Observer<Boolean> { isLoginError ->
            if (isLoginError) onLoginFail()
        })

        viewModel.eventCreationSuccess.observe(this, Observer<Boolean> { success ->
            if (success) onCreateSuccess()
        })





    }

    private fun createCandidateAcademicInfo(request: CreateAcademicInfoRequest) {
        lifecycleScope.launch {
            viewModel.createCandidateAcademicInfo(request)

        }
    }

    private fun onLoginFail() {
        Log.i("onLoginFailAcademicInfo", ""+!viewModel.isUnSuccessShownToCreateAcademicInfo.value!!)
        if(!viewModel.isUnSuccessShownToCreateAcademicInfo.value!!) {
            Toast.makeText(applicationContext, resources.getString(R.string.sesionCerrada), Toast.LENGTH_LONG).show()
            viewModel.onUnSuccessLoginShownToCreateAcademicInfo()
        }
    }

    private fun onNetworkError() {
        Log.i("onNetworkError", ""+!viewModel.isNetworkErrorShown.value!!)
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(applicationContext, resources.getString(R.string.networkError), Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
    private fun onCreateSuccess() {
        Log.i("CreateAcademicInfoOK", ""+!viewModel.isSuccessShownToCreateAcademicInfo.value!!)
        if(!viewModel.isSuccessShownToCreateAcademicInfo.value!!) {
            Log.i("ShowMessageToCreateAI", ""+!viewModel.isSuccessShownToCreateAcademicInfo.value!!)
            Toast.makeText(applicationContext, resources.getString(R.string.InformacionAcademicaCreada), Toast.LENGTH_LONG).show()

            viewModel.onSuccessCreateAcademiInfoShown()

            val intent = Intent(this, CandidateOptionsMyData::class.java)
            startActivity(intent)
        }

    }

    private fun openCancelDialog(intent: Intent){
        val builder = AlertDialog.Builder(this@CreateCandidateAcademicInfoActivity)
        builder.setMessage(R.string.ConfirmationToCancelCreateAcademicInfo)
        builder.setTitle(R.string.Advertencia)
        builder.setCancelable(false)
        builder.setPositiveButton(R.string.ConfirmationSi,
            DialogInterface.OnClickListener { _: DialogInterface?, _: Int ->
                val intent = Intent(this, CandidateOptionsMyData::class.java)
                startActivity(intent)
            })

        builder.setNegativeButton(R.string.ConfirmationNo,
            DialogInterface.OnClickListener { dialog: DialogInterface, _: Int ->
                dialog.cancel()
            } as DialogInterface.OnClickListener)

        val alertDialog = builder.create()
        alertDialog.show()
    }
}