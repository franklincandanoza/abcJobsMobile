package com.uniandes.abcjobs.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.uniandes.abcjobs.R
import com.uniandes.abcjobs.databinding.ActivityCreateCandidateBinding
import com.uniandes.abcjobs.models.CandidateRequest
import com.uniandes.abcjobs.viewmodels.CandidateViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class CreateCandidateActivity : AppCompatActivity(){

    private lateinit var viewModel: CandidateViewModel
    /*private var canadidateAdapter: CandidateAdapter? = null*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityCreateCandidateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //candidateAdapter = CandidateAdapter()
        viewModel = ViewModelProvider(this).get(CandidateViewModel::class.java)

        var createCandidateButton: CardView = binding.CreateCandidateButton

        var cancelCreateCandidateButton: CardView = binding.cancelCreateCandidateButton

        createCandidateButton.setOnClickListener {

            // Do click handling here
            var emailEditText: EditText = findViewById(R.id.Email)
            var candidateEmail = emailEditText.text.toString()

            var passwordEditText: EditText = findViewById(R.id.password)
            var candidatePassword = passwordEditText.text.toString()

            var typeSpinner: Spinner = findViewById(R.id.documentType)
            var candidateTypeDoc = typeSpinner.selectedItem.toString().split("-").get(1).toString()

            var documentEditText: EditText = findViewById(R.id.document)
            var candidateDocument = documentEditText.text.toString()


            var firstNameEditText: EditText = findViewById(R.id.firstName)
            var candidateName = firstNameEditText.text.toString()

            var lastNameEditText: EditText = findViewById(R.id.lastName)
            var candidateLastName = lastNameEditText.text.toString()

            var ageSpinner: Spinner = findViewById(R.id.Age)
            var candidateAge = ageSpinner.selectedItem.toString()

            var addressEditText: EditText = findViewById(R.id.address)
            var candidateAddress = addressEditText.text.toString()

            var cityEditText: EditText = findViewById(R.id.city)
            var candidateCity = cityEditText.text.toString()

            var residenceSpinner: Spinner = findViewById(R.id.residenceCountry)
            var candidateResidence = residenceSpinner.selectedItem.toString().split("-").get(1).toString()

            var originSpinner: Spinner = findViewById(R.id.originCountry)
            var candidateOrigin = originSpinner.selectedItem.toString().split("-").get(1).toString()


            var phoneEditText: EditText = findViewById(R.id.phoneNumber)
            var candidatePhone = phoneEditText.text.toString()

            var candidateRole = "CANDIDATE";
            var candidateBD= "2001-01-01";

            var policyCheck: CheckBox = findViewById(R.id.policy)

            if(candidateEmail.isEmpty() || !isValidEmail(candidateEmail)){
                emailEditText.error = resources.getString(R.string.correoInvalido)
                return@setOnClickListener
            }
            if(candidatePassword.isEmpty() || !isValidPassword(candidatePassword)){
                passwordEditText.error = resources.getString(R.string.claveInvalida)
                return@setOnClickListener
            }
            if(candidateDocument.isEmpty() || !isValidNumber(candidateDocument,6,30)){
                documentEditText.error = resources.getString(R.string.documentoInvalido)
                return@setOnClickListener
            }

            if(candidateName.isEmpty() || !isValidName(candidateName)){
                firstNameEditText.error = resources.getString(R.string.nombreInvalido)
                return@setOnClickListener
            }
            if(candidateLastName.isEmpty() || !isValidName(candidateLastName)){
                lastNameEditText.error = resources.getString(R.string.apellidoInvalido)
                return@setOnClickListener
            }
            if(candidateAddress.isEmpty() || !isValidAddress(candidateAddress)){
                addressEditText.error = resources.getString(R.string.direccionInvalida)
                return@setOnClickListener
            }
            if(candidateCity.isEmpty() || !isValidName(candidateCity)){
                cityEditText.error = resources.getString(R.string.ciudadInvalida)
                return@setOnClickListener
            }
            if(candidatePhone.isEmpty() || !isValidNumber(candidatePhone,8,30)){
                phoneEditText.error = resources.getString(R.string.telefonoInvalido)
                return@setOnClickListener
            }
            if(!policyCheck.isChecked)
            {
                policyCheck.error =resources.getString(R.string.politicaInvalida)
                policyCheck.requestFocus()
                return@setOnClickListener
            }

            var candidateRequest = CandidateRequest(
                candidateDocument,
                candidateTypeDoc,
                candidateName,
                candidateLastName,
                candidatePhone,
                candidateEmail,
                candidatePassword,
                candidateBD,
                candidateAge.toInt(),
                candidateOrigin,
                candidateResidence,
                candidateCity,
                candidateAddress
            )

            //println("Enviando Peticion $candidateDocument, $candidateTypeDoc $candidateOrigin $candidateResidence")
            createCandidate(candidateRequest)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            

        }

        cancelCreateCandidateButton.setOnClickListener {
            openCancelDialog(Intent(this, MainActivity::class.java))
        }

    }
    private fun createCandidate(candidateRequest: CandidateRequest) {
        lifecycleScope.launch {
          var message:String =""
          val responseCode = viewModel.createCandidate(candidateRequest)

            if (responseCode.length == 0)
                message=resources.getString(R.string.candidatoCreado)

            val toast =
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)



            toast.show()
        }

    }
    fun isValidPassword(password: String?) : Boolean {
        password?.let {
            val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$"
            val passwordMatcher = Regex(passwordPattern)

            return passwordMatcher.find(password) != null
        } ?: return false
    }

    fun isValidEmail(email: String?) : Boolean {
        email?.let {
            val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
            val emailMatcher = Regex(emailPattern)

            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } ?: return false
    }
    fun isValidNumber(num: String?, min: Int?, max: Int?) : Boolean {
        num?.let {
            val numberPattern = "[0-9]{" + min.toString() + "," + max.toString() + "}$"
            val numberMatcher = Regex(numberPattern)

            return numberMatcher.find(num) != null
        } ?: return false
    }
    fun isValidName(name: String?) : Boolean {
        name?.let {
            val namePattern = "(?=^.{2,30}$)(?=.*[A-Z]|[a-z]).*$"
            val nameMatcher = Regex(namePattern)

            return nameMatcher.find(name) != null
        } ?: return false
    }
    fun isValidAddress(address: String?) : Boolean {
        address?.let {
            val addressPattern = "(?=^.{8,60}$)(?=.*[A-Z]|[a-z]).*$"
            val addressMatcher = Regex(addressPattern)

            return addressMatcher.find(address) != null
        } ?: return false
    }
    private fun openCancelDialog(intent: Intent){
        val builder = AlertDialog.Builder(this@CreateCandidateActivity)
        builder.setMessage("Esta seguro de cancelar la creacion del Candidato?")
        builder.setTitle("Advertencia!")
        builder.setCancelable(false)
        builder.setPositiveButton("Si",
            DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                startActivity(intent)
            })

        builder.setNegativeButton("No",
            DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                dialog.cancel()
            } as DialogInterface.OnClickListener)

        val alertDialog = builder.create()
        alertDialog.show()
    }
}