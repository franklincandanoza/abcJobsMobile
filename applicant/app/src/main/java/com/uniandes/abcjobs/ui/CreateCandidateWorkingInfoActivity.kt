package com.uniandes.abcjobs.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.uniandes.abcjobs.R
import com.uniandes.abcjobs.databinding.ActivityCreateCandidateWorkingInfoBinding

import com.uniandes.abcjobs.models.CreateWorkingInfoRequest
import com.uniandes.abcjobs.viewmodels.CandidateViewModel
import kotlinx.coroutines.launch


class CreateCandidateWorkingInfoActivity : AppCompatActivity(){

    private lateinit var viewModel: CandidateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityCreateCandidateWorkingInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //candidateAdapter = CandidateAdapter()
        viewModel = ViewModelProvider(this).get(CandidateViewModel::class.java)

        var createWorkingInfoButton: CardView = binding.createCandidateWorkingInfoButton

        var cancelCreateWorkingInfoButton: CardView = binding.cancelCreateCandidateWorkingInfoButton

        var currentCheckBox: CheckBox = binding.vigente

        var endMonthSpinner: Spinner = binding.endMonth
        var endYearSpinner: Spinner = binding.endYear

        currentCheckBox.setOnClickListener {

            endMonthSpinner.isEnabled = !currentCheckBox.isChecked
            endYearSpinner.isEnabled = !currentCheckBox.isChecked

        }

        createWorkingInfoButton.setOnClickListener {

            // Do click handling here
            var positionEditText: EditText = findViewById(R.id.workingInfoPosition)
            var candidatePosition = positionEditText.text.toString()

            var companyEditText: EditText = findViewById(R.id.workingInfoCompany)
            var candidateCompany = companyEditText.text.toString()

            var countrySpinner: Spinner = findViewById(R.id.country)
            var companyCountry = countrySpinner.selectedItem.toString().split("-").get(1).toString()

            var startMonthSpinner: Spinner = findViewById(R.id.startMonth)
            var candidateStartMonth = startMonthSpinner.selectedItem.toString().toInt()

            var startYearSpinner: Spinner = findViewById(R.id.startYear)
            var candidateStartYear = startYearSpinner.selectedItem.toString().toInt()

            var candidateEndMonth = endMonthSpinner.selectedItem.toString().toInt()

            var candidateEndYear = endYearSpinner.selectedItem.toString().toInt()

            var addressEditText: EditText = findViewById(R.id.workingInfoAddress)
            var companyAddress = addressEditText.text.toString()

            var telephoneEditText: EditText = findViewById(R.id.workingInfoTelephone)
            var companyTelephone = telephoneEditText.text.toString()

            var descriptionEditText: EditText = findViewById(R.id.description)
            var candidateDescription = descriptionEditText.text.toString()



            if(candidatePosition.isEmpty()){
                positionEditText.error = resources.getString(R.string.workingInfoInvalidPosition)
                return@setOnClickListener
            }

            if(candidateCompany.isEmpty()){
                companyEditText.error = resources.getString(R.string.workingInfoInvalidCompany)
                return@setOnClickListener
            }

            if(companyAddress.isEmpty()){
                addressEditText.error = resources.getString(R.string.workingInfoInvalidAddress)
                return@setOnClickListener
            }

            if(companyTelephone.isEmpty()){
                telephoneEditText.error = resources.getString(R.string.workingInfoInvalidTelephone)
                return@setOnClickListener
            }

            if(candidateDescription.isEmpty()){
                descriptionEditText.error = resources.getString(R.string.workingInfoInvalidDescription)
                return@setOnClickListener
            }


            var createWorkingInfoRequest = CreateWorkingInfoRequest(
                candidatePosition,
                candidateCompany,
                companyCountry,
                candidateStartMonth,
                candidateStartYear,
                if (currentCheckBox.isChecked) null else candidateEndMonth,
                if (currentCheckBox.isChecked) null else candidateEndYear,
                companyAddress,
                companyTelephone,
                candidateDescription,
            )

            createCandidateWorkingInfo(createWorkingInfoRequest)

        }

        cancelCreateWorkingInfoButton.setOnClickListener {
            openCancelDialog(Intent(this, MainActivity::class.java))
        }

        createObservers()

    }

    private fun createObservers(){
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

    private fun createCandidateWorkingInfo(request: CreateWorkingInfoRequest) {
        lifecycleScope.launch {
            viewModel.createCandidateWorkingInfo(request)

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
            Toast.makeText(applicationContext, resources.getString(R.string.InformacionLaboralCreada), Toast.LENGTH_LONG).show()

            viewModel.onSuccessCreateAcademiInfoShown()

            val intent = Intent(this, CandidateOptionsMyDataActivity::class.java)
            startActivity(intent)
        }

    }

    private fun openCancelDialog(intent: Intent){
        val builder = AlertDialog.Builder(this@CreateCandidateWorkingInfoActivity)
        builder.setMessage(R.string.ConfirmationToCancelCreateWorkingInfo)
        builder.setTitle(R.string.Advertencia)
        builder.setCancelable(false)
        builder.setPositiveButton(R.string.ConfirmationSi,
            DialogInterface.OnClickListener { _: DialogInterface?, _: Int ->
                val intent = Intent(this, CandidateOptionsMyDataActivity::class.java)
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