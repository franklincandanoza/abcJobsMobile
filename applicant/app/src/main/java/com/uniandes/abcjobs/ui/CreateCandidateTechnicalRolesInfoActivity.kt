package com.uniandes.abcjobs.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Observer
import com.uniandes.abcjobs.R
import com.uniandes.abcjobs.databinding.ActivityCreateCandidateTechnicalRolesBinding
import com.uniandes.abcjobs.viewmodels.CandidateViewModel
import com.uniandes.abcjobs.models.CreateCandidateTechnicalRoleInfoRequest
import kotlinx.coroutines.launch

class CreateCandidateTechnicalRolesInfoActivity : AppCompatActivity() {

    private lateinit var viewModel: CandidateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityCreateCandidateTechnicalRolesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //candidateAdapter = CandidateAdapter()
        viewModel = ViewModelProvider(this).get(CandidateViewModel::class.java)

        var createCandidateTechnicalRoleButton: CardView = binding.createCandidateTechnicalRoleButton

        var cancelCreateCandidateTechnicalRoleButton: CardView = binding.cancelCreateCandidateTechnicalRoleButton

        createCandidateTechnicalRoleButton.setOnClickListener {
            // Do click handling here
            var nameEditText: EditText = findViewById(R.id.name)
            var name = nameEditText.text.toString()

            var experienceEditText: EditText = findViewById(R.id.experience)
            var experience = if (experienceEditText.text.toString().isEmpty()) 0 else experienceEditText.text.toString().toInt()


            var descriptionEditText: EditText = findViewById(R.id.description)
            var description = descriptionEditText.text.toString()

            if(name.isEmpty()){
                nameEditText.error = resources.getString(R.string.RoleTecnicoInvalido)
                return@setOnClickListener
            }

            if(experience<=0){
                experienceEditText.error = resources.getString(R.string.experienciaRoleTecnicoInvalida)
                return@setOnClickListener
            }

            if(description.isEmpty()){
                descriptionEditText.error = resources.getString(R.string.descriptionInvalidaRoleTecnico)
                return@setOnClickListener
            }

            var createTechnicalRoleInfoRequest = CreateCandidateTechnicalRoleInfoRequest(
                name,
                experience,
                description
            )

            createTechnicalRoleInfo(createTechnicalRoleInfoRequest)

        }

        cancelCreateCandidateTechnicalRoleButton.setOnClickListener {
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

    private fun onCreateSuccess() {
        Log.i("CreateAcademicInfoOK", ""+!viewModel.isSuccessShownToCreateAcademicInfo.value!!)
        if(!viewModel.isSuccessShownToCreateAcademicInfo.value!!) {
            Log.i("ShowMessageToCreateAI", ""+!viewModel.isSuccessShownToCreateAcademicInfo.value!!)
            Toast.makeText(applicationContext, resources.getString(R.string.roleTecnicoCreado), Toast.LENGTH_LONG).show()

            viewModel.onSuccessCreateAcademiInfoShown()

            val intent = Intent(this, CandidateOptionsTechnicalInfo::class.java)
            startActivity(intent)
        }

    }

    private fun createTechnicalRoleInfo(request: CreateCandidateTechnicalRoleInfoRequest) {
        lifecycleScope.launch {
            viewModel.createCandidateRoleTechnicalInfo(request)

        }
    }

    private fun openCancelDialog(intent: Intent){
        val builder = AlertDialog.Builder(this@CreateCandidateTechnicalRolesInfoActivity)
        builder.setMessage(R.string.ConfirmationToCancelCreateTechnicalRoleInfo)
        builder.setTitle(R.string.Advertencia)
        builder.setCancelable(false)
        builder.setPositiveButton(R.string.ConfirmationSi,
            DialogInterface.OnClickListener { _: DialogInterface?, _: Int ->
                val intent = Intent(this, CandidateOptionsTechnicalInfo::class.java)
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