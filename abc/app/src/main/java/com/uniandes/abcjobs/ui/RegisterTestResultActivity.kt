package com.uniandes.abcjobs.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.uniandes.abcjobs.R
import com.uniandes.abcjobs.databinding.ActivityRegisterTestResultBinding
import com.uniandes.abcjobs.models.RegisterResultTestRequest
import com.uniandes.abcjobs.models.Test
import com.uniandes.abcjobs.viewmodels.CandidateViewModel
import com.uniandes.abcjobs.viewmodels.RegisterTestResultViewModel
import kotlinx.coroutines.launch
import java.util.*


class RegisterTestResultActivity : AppCompatActivity(){

    private lateinit var viewModel: RegisterTestResultViewModel

    private lateinit var viewModel2: CandidateViewModel

    private lateinit var tests:List<Test>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityRegisterTestResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(RegisterTestResultViewModel::class.java)

        viewModel2=ViewModelProvider(this).get(CandidateViewModel::class.java)

        viewModel.tests.observe(this,{
            if (it!=null){
                tests=it
                var testSpinner: Spinner = findViewById(R.id.tests)
                val tests = it?.map { it.name }?.toTypedArray()
                val arrayAdapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, tests)
                testSpinner.adapter = arrayAdapter
            }
        })
        var testSpinner: Spinner = findViewById(R.id.tests)
        testSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                var testSpinner: Spinner = findViewById(R.id.tests)
                var test: String = testSpinner.selectedItem.toString()

                var  technology:String? =""
                tests.forEach { element ->
                        if(element.name==test){
                            technology=element.technology
                        }
                }
                testSpinner.selectedItem.toString()

                var technologySpinner: Spinner = findViewById(R.id.technology)
                technologySpinner.isEnabled=false
                for (i in 0..technologySpinner.getCount()-1) {
                    if (technologySpinner.getItemAtPosition(i).equals(technology)) {
                        technologySpinner.setSelection(i)
                    }
                }
            }
        }

        var dateEditText: DatePicker = findViewById(R.id.date)
        dateEditText.maxDate=System.currentTimeMillis()
        dateEditText.minDate=1672598368000
        var registerTestResultButton: CardView = binding.RegisterTestResultButton

        var cancelCreateCandidateButton: CardView = binding.cancelCreateCandidateButton
        var documentEditText: EditText = findViewById(R.id.document)
        documentEditText.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {

                viewModel2.findCandidate(documentEditText.text.toString())
            }
        })

        viewModel2.candidate.observe(this,{
            var candidateNameEditText: EditText = findViewById(R.id.candidateName)
            if (it!=null){

                candidateNameEditText.setText(it.fullName)
            }
            else{
                candidateNameEditText.setText("")
                documentEditText.error = resources.getString(R.string.documentoNameNotFound)
                show_error( resources.getString(R.string.documentoNameNotFound))
            }
        })

        registerTestResultButton.setOnClickListener {

            // Do click handling here
            var testSpinner: Spinner = findViewById(R.id.tests)
            var test = testSpinner.selectedItem.toString()

            //var technologySpinner: Spinner = findViewById(R.id.technology)
            //var technology = technologySpinner.selectedItem.toString()

            var dateEditText: DatePicker = findViewById(R.id.date)
            var testDate = dateEditText.dayOfMonth.toString()

            var documentEditText: EditText = findViewById(R.id.document)
            var candidateDocument = documentEditText.text.toString()

            var documentNameEditText: EditText = findViewById(R.id.document)
            var candidateName = documentNameEditText.text.toString()

            var pointsEditText: EditText = findViewById(R.id.editTextPoints)
            var points = pointsEditText.text.toString()

            var observationEditText: EditText = findViewById(R.id.editObservation)
            var observation = observationEditText.text.toString()


            if(test.isEmpty()){

                (testSpinner.getSelectedView() as TextView).error = "Error message"
                return@setOnClickListener
            }

            if(candidateDocument.isEmpty() || !isValidNumber(candidateDocument,6,30)){
                documentEditText.error = resources.getString(R.string.documentoInvalido)
                return@setOnClickListener
            }

            if(candidateName.isEmpty() || !isValidNumber(candidateName,1,500)){
                documentEditText.error = resources.getString(R.string.documentoNameNotFound)
                return@setOnClickListener
            }


            if(testDate.isEmpty()){// || !isValidName(testDate)){
                return@setOnClickListener
            }

            if(points.isEmpty()  || !isValidNumber(points,0,3) || !isValidNumber(points.toInt(),0,100)){
                pointsEditText.error = resources.getString(R.string.PuntajeInvalido)
                return@setOnClickListener
            }

            var registerTestRequest = RegisterResultTestRequest(
                test,
                candidateDocument,
                observation,
                points.toInt()
            )

            //println("Enviando Peticion $candidateDocument, $candidateTypeDoc $candidateOrigin $candidateResidence")
            registerTest(registerTestRequest)
            val intent = Intent(this, CompanyOptionsActivity::class.java)
            startActivity(intent)
            

        }

        cancelCreateCandidateButton.setOnClickListener {
            openCancelDialog(Intent(this, CompanyOptionsActivity::class.java))
        }

        viewModel.enabledTest()
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
        val builder = AlertDialog.Builder(this@RegisterTestResultActivity)
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
