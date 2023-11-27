package com.uniandes.abcjobs.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.uniandes.abcjobs.models.Company
import com.uniandes.abcjobs.models.InterviewResult
import com.uniandes.abcjobs.models.Project
import com.uniandes.abcjobs.models.RegisterResultTestRequest
import com.uniandes.abcjobs.repositories.TestRepository
import com.uniandes.abcjobs.repositories.CompanyRepository
import com.uniandes.abcjobs.repositories.ProjectRepository
import com.uniandes.abcjobs.repositories.InterviewRepository
import com.uniandes.abcjobs.repositories.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InterviewViewModel(application: Application) :  AndroidViewModel(application) {

    val ERROR_RESPONSE_MESSAGE = "ErrorResponse"

    private val testRepo = TestRepository()

    private val companyRepo = CompanyRepository()

    private val interviewRepo = InterviewRepository()

    private val loginRepository = LoginRepository()

    private val projectRepo = ProjectRepository()

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    private var _isNetworkErrorShownForCreateAlbum = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    private val companiesMutableData = MutableLiveData<List<Company>>()
    val companies: LiveData<List<Company>>
        get() = companiesMutableData

    private val projectsMutableData = MutableLiveData<List<Project>>()
    val projects: LiveData<List<Project>>
        get() = projectsMutableData


    fun getCompanies() {
        viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.IO) {
                companyRepo.getCompanies({
                    Log.d("Info", it.toString())
                    companiesMutableData.postValue(it)
                }, {
                    it.printStackTrace()
                    Log.d("Error", it.toString())
                })
            }
        }
    }

    fun getProjectsByCompany(companyId: String) {
        projectsMutableData.postValue(emptyList())
        viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.IO) {
                projectRepo.getProjectsByCompany(companyId, {
                    Log.d("Interview", it.toString())

                    projectsMutableData.postValue(it)
                }, {
                    it.printStackTrace()
                    Log.d("Error", it.toString())
                })
            }
        }
    }

    suspend fun getResultsByProject(projectId: String, onComplete:(resp: List<InterviewResult>)->Unit,
                                  onError: (error: Exception)->Unit){

        Log.i("Interview", "Consultando resultados para el proyecto")
        viewModelScope.launch (Dispatchers.Default ){
            withContext(Dispatchers.IO){
                loginRepository.whoIAm({ response ->
                    var personId = response.personId
                    viewModelScope.launch (Dispatchers.Default){
                        withContext(Dispatchers.IO) {
                            if (personId != null) {
                                interviewRepo.getResultsByProject(projectId,
                                    {
                                        onComplete(it)
                                    },
                                    {
                                        handleException(it)
                                    }
                                )
                            }
                        }
                    }
                },{
                    handleException(it)
                })
            }
        }

    }

    private fun handleException(exception: Exception){
        Log.d("Error", exception.toString())
        if (exception is retrofit2.HttpException){
            val err = exception as retrofit2.HttpException
            Log.d(ERROR_RESPONSE_MESSAGE, err.response()?.code().toString())
            if (err.response()?.code()==401){
                Log.d(ERROR_RESPONSE_MESSAGE, "response code 401 trying to create academic info")
            }else{
                Log.d(ERROR_RESPONSE_MESSAGE, "response code else")
                _eventNetworkError.postValue(true)
                _isNetworkErrorShown.postValue(false)
            }
        }else{
            _eventNetworkError.postValue(true)
            _isNetworkErrorShown.postValue(false)
        }
    }


    fun registerTestResult(request : RegisterResultTestRequest): String {
        var responseCode="";
        try {
            viewModelScope.launch (Dispatchers.Default){
                withContext(Dispatchers.IO) {
                    testRepo.registerTestRequest(testResultToJsonObject(request))
                }
            }

        }
        catch (e:Exception){
            println("Error creando Candidato")
            responseCode = e.message.toString()
        }
        return responseCode
    }



    private fun testResultToJsonObject(registerResultTestRequest: RegisterResultTestRequest): JsonArray {
        val paramObject = JsonObject()
        val jsonArray = JsonArray()

        paramObject.addProperty("test_name", registerResultTestRequest.test_name)
        paramObject.addProperty("candidate_document", registerResultTestRequest.candidate_document)
        paramObject.addProperty("points", registerResultTestRequest.points)
        paramObject.addProperty("observation", registerResultTestRequest.observation)
        jsonArray.add(paramObject)
        return jsonArray
    }
}