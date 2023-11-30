package com.uniandes.abcjobs.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.AppBarConfiguration
import com.uniandes.abcjobs.R
import com.uniandes.abcjobs.databinding.ActivityMainBinding
import com.uniandes.abcjobs.models.LoginRequest
import com.uniandes.abcjobs.viewmodels.LoginViewModel
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var locale: Locale
    private var currentLanguage = "es"
    private var currentLang: String? = null
    companion object {
        public var dLocale: Locale? = null
    }
    private lateinit var viewModel: LoginViewModel
    //candidateAdapter = CandidateAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentLanguage = intent.getStringExtra(currentLang).toString()

        var languageSp: Spinner =  findViewById(R.id.spinnerLanguage)
        languageSp?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    1 -> setLocale("es")
                    2 -> setLocale("en")
                }
                //println("********************************************")
                //println("Opcion escogida: $position")

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, CreateCandidateActivity::class.java)
            startActivity(intent)
        }

        binding.cardAceptar.setOnClickListener{
            // Do click handling here
            var editTextUser: EditText = findViewById(R.id.editTextUser)
            var userName = editTextUser.text.toString()

            var passwordEditText: EditText = findViewById(R.id.editTextPassword)
            var password = passwordEditText.text.toString()

            if(password.isEmpty()){
                passwordEditText.error = resources.getString(R.string.claveInvalida)
                return@setOnClickListener
            }
            if(userName.isEmpty() ){
                editTextUser.error = resources.getString(R.string.usuarioInvalido)
                return@setOnClickListener
            }

            var loginRequest=LoginRequest(userName,password)
            login(loginRequest)


        }
        binding.cardCancelar.setOnClickListener{
            var editTextUser: EditText = findViewById(R.id.editTextUser)
            editTextUser.text.clear()

            var passwordEditText: EditText = findViewById(R.id.editTextPassword)
            passwordEditText.text.clear()
            finishAffinity()
            finish()
            System.exit(0)
        }

        viewModel.eventNetworkError.observe(this, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

        viewModel.eventLoginFail.observe(this, Observer<Boolean> { isLoginError ->
            if (isLoginError) onLoginFail()
        })
        viewModel.eventLoginSuccess.observe(this, Observer<Boolean> { isLoginSuccessful ->
            if (isLoginSuccessful) onLoginSuccess()
        })
    }

    private fun login(loginRequest: LoginRequest) {
        lifecycleScope.launch {
            viewModel.login(loginRequest)

        }

    }

    private fun setLocale(localeName: String) {
        if (localeName != currentLanguage) {
            println("**** Ingresando a cambiar idioma ${localeName}")
            locale = Locale(localeName)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)
            Locale.setDefault(locale)
            val refresh = Intent(
                this,
                MainActivity::class.java
            )
            refresh.putExtra(currentLang, localeName)
            startActivity(refresh)
            finish()
        } else {
            Toast.makeText(
                this@MainActivity, "Language, already, selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private fun onNetworkError() {
        Log.i("onNetworkError", ""+!viewModel.isNetworkErrorShown.value!!)
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(applicationContext, resources.getString(R.string.networkError), Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    private fun onLoginFail() {
        Log.i("onNetworkError", ""+!viewModel.isUnSuccessShown.value!!)
        if(!viewModel.isUnSuccessShown.value!!) {
            Toast.makeText(applicationContext, resources.getString(R.string.invalidCredentials), Toast.LENGTH_LONG).show()
            viewModel.onUnSuccessLoginShown()
        }
    }

    private fun onLoginSuccess() {
        Log.i("onNetworkError", ""+!viewModel.isSuccessShown.value!!)
        if(!viewModel.isSuccessShown.value!!) {
            Toast.makeText(applicationContext, resources.getString(R.string.loginSuccess), Toast.LENGTH_LONG).show()

            viewModel.onSuccessLoginShown()

            var editTextUser: EditText = findViewById(R.id.editTextUser)
            editTextUser.text.clear()

            var passwordEditText: EditText = findViewById(R.id.editTextPassword)
            passwordEditText.text.clear()

            val intent = Intent(this, CandidateOptionsActivity::class.java)
            startActivity(intent)
        }
    }
}