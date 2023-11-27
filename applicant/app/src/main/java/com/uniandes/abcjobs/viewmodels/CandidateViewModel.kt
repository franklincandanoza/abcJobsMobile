package com.uniandes.abcjobs.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.uniandes.abcjobs.models.Candidate
import com.uniandes.abcjobs.models.CandidateRequest
import com.uniandes.abcjobs.models.CreateAcademicInfoRequest
import com.uniandes.abcjobs.models.CreateCandidateTechnicalRoleInfoRequest
import com.uniandes.abcjobs.models.CreateCandidateTechnologyInfoRequest
import com.uniandes.abcjobs.models.CreateWorkingInfoRequest
import com.uniandes.abcjobs.models.Interview
import com.uniandes.abcjobs.repositories.CandidateRepository
import com.uniandes.abcjobs.repositories.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CandidateViewModel(application: Application) :  AndroidViewModel(application) {

    val ERROR_RESPONSE_MESSAGE = "ErrorResponse"

    private val candidatesRepo = CandidateRepository()

    private val loginRepository = LoginRepository()

    private val candidatesMutableData = MutableLiveData<List<Candidate>>()

    private val interviewsMutableData = MutableLiveData<List<Interview>>()

    private var _eventLoginFail = MutableLiveData<Boolean>(false)
    val eventLoginFail: LiveData<Boolean>
        get() = _eventLoginFail

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _eventCreationSuccess = MutableLiveData<Boolean>(false)
    val eventCreationSuccess: LiveData<Boolean>
        get() = _eventCreationSuccess

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    private var _isSuccessShownToCreateAcademicInfo = MutableLiveData<Boolean>(false)

    val isSuccessShownToCreateAcademicInfo: LiveData<Boolean>
        get() = _isSuccessShownToCreateAcademicInfo

    private var _isUnSuccessShownToCreateAcademicInfo = MutableLiveData<Boolean>(false)

    val isUnSuccessShownToCreateAcademicInfo: LiveData<Boolean>
        get() = _isUnSuccessShownToCreateAcademicInfo

    fun onUnSuccessLoginShownToCreateAcademicInfo() {
        _isUnSuccessShownToCreateAcademicInfo.value = true
    }
    fun onSuccessCreateAcademiInfoShown() {
        _isSuccessShownToCreateAcademicInfo.value = true
    }

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        //refreshCandidates()
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }


    private fun refreshCandidates() {
        viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.IO) {
                candidatesRepo.refreshData({
                    candidatesMutableData.postValue(it)
                    _eventNetworkError.postValue(false)
                    _isNetworkErrorShown.postValue(false)
                }, {
                    Log.d("Error", it.toString())
                    _eventNetworkError.postValue(true)
                })
            }
        }
    }

    suspend fun refreshInterviews(onComplete:(resp: List<Interview>)->Unit,
                          onError: (error: Exception)->Unit){

        viewModelScope.launch (Dispatchers.Default ){
            withContext(Dispatchers.IO){
                loginRepository.whoIAm({ response ->
                    var personId = response.personId
                    viewModelScope.launch (Dispatchers.Default){
                        withContext(Dispatchers.IO) {
                            if (personId != null) {
                                candidatesRepo.getMyInterviews(personId.toInt(),
                                    {
                                        onComplete(it)
                                        _isSuccessShownToCreateAcademicInfo.postValue(false)
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

    fun createCandidate(request : CandidateRequest): String {
        var responseCode="";
        try {
            viewModelScope.launch (Dispatchers.Default){
                withContext(Dispatchers.IO) {
                    candidatesRepo.createCandidate(candidateToJsonObject(request))
                }
            }

        }
        catch (e:Exception){
            println("Error creando Candidato")
            responseCode = e.message.toString()
        }
        return responseCode
    }

    fun createCandidateAcademicInfo(request : CreateAcademicInfoRequest) {

        viewModelScope.launch (Dispatchers.Default ){
            withContext(Dispatchers.IO){
                loginRepository.whoIAm({ response ->
                    var token = response.token
                    viewModelScope.launch (Dispatchers.Default){
                        withContext(Dispatchers.IO) {
                            if (token != null) {
                                candidatesRepo.createCandidateAcademicInfo(candidateAcademicInfoToJsonObject(request),token,
                                    {
                                        it.msg?.let { it1 -> Log.d("Success", it1) }

                                        _eventCreationSuccess.postValue(true)
                                        _isSuccessShownToCreateAcademicInfo.postValue(false)
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

    fun createCandidateWorkingInfo(request : CreateWorkingInfoRequest) {

        viewModelScope.launch (Dispatchers.Default ){
            withContext(Dispatchers.IO){
                loginRepository.whoIAm({ response ->
                    var token = response.token
                    viewModelScope.launch (Dispatchers.Default){
                        withContext(Dispatchers.IO) {
                            if (token != null) {
                                candidatesRepo.createCandidateWorkingInfo(candidateWorkingInfoToJsonObject(request),token,
                                    {
                                        it.msg?.let { it1 -> Log.d("Success", it1) }

                                        _eventCreationSuccess.postValue(true)
                                        _isSuccessShownToCreateAcademicInfo.postValue(false)
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

    fun createCandidateRoleTechnicalInfo(request : CreateCandidateTechnicalRoleInfoRequest) {

        viewModelScope.launch (Dispatchers.Default ){
            withContext(Dispatchers.IO){
                loginRepository.whoIAm({ response ->
                    var token = response.token
                    viewModelScope.launch (Dispatchers.Default){
                        withContext(Dispatchers.IO) {
                            if (token != null) {
                                candidatesRepo.createCandidateTechnicalRoleInfo(candidateTechnicalRoleInfoToJsonObject(request),token,
                                    {
                                        it.msg?.let { it1 -> Log.d("Success", it1) }

                                        _eventCreationSuccess.postValue(true)
                                        _isSuccessShownToCreateAcademicInfo.postValue(false)
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

    fun createCandidateTechnologyInfo(request : CreateCandidateTechnologyInfoRequest) {

        viewModelScope.launch (Dispatchers.Default ){
            withContext(Dispatchers.IO){
                loginRepository.whoIAm({ response ->
                    var token = response.token
                    viewModelScope.launch (Dispatchers.Default){
                        withContext(Dispatchers.IO) {
                            if (token != null) {
                                candidatesRepo.createCandidateTechnologyInfo(candidateTechnologyInfoToJsonObject(request),token,
                                    {
                                        it.msg?.let { it1 -> Log.d("Success", it1) }

                                        _eventCreationSuccess.postValue(true)
                                        _isSuccessShownToCreateAcademicInfo.postValue(false)
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
                _eventLoginFail.postValue(true)
                _isUnSuccessShownToCreateAcademicInfo.postValue(true)
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



    private fun candidateToJsonObject(candidate: CandidateRequest): JsonObject {
        val paramObject = JsonObject()
        paramObject.addProperty("document", candidate.document)
        paramObject.addProperty("documentType", candidate.documentType)
        paramObject.addProperty("firstName", candidate.firstName)
        paramObject.addProperty("lastName", candidate.lastName)
        paramObject.addProperty("phoneNumber", candidate.phoneNumber)
        paramObject.addProperty("username", candidate.username)
        paramObject.addProperty("password", candidate.password)
        paramObject.addProperty("role", "CANDIDATE")
        paramObject.addProperty("birthDate", "2001-01-01")
        paramObject.addProperty("age", candidate.age)
        paramObject.addProperty("originCountry", candidate.originCountry)
        paramObject.addProperty("residenceCountry", candidate.residenceCountry)
        paramObject.addProperty("residenceCity", candidate.residenceCity)
        paramObject.addProperty("address", candidate.address)
        return paramObject
    }

    private fun candidateAcademicInfoToJsonObject(academicInfoRequest: CreateAcademicInfoRequest): JsonObject {
        val paramObject = JsonObject()
        paramObject.addProperty("title", academicInfoRequest.title)
        paramObject.addProperty("institution", academicInfoRequest.institution)
        paramObject.addProperty("country", academicInfoRequest.country)
        paramObject.addProperty("year_start_date", academicInfoRequest.yearStartDate)
        paramObject.addProperty("month_start_date", academicInfoRequest.monthStartDate)
        paramObject.addProperty("year_end_date", academicInfoRequest.yearEndDate)
        paramObject.addProperty("month_end_date", academicInfoRequest.monthEndDate)
        paramObject.addProperty("description", academicInfoRequest.description)
        return paramObject
    }

    private fun candidateWorkingInfoToJsonObject(workingInfoRequest: CreateWorkingInfoRequest): JsonObject {
        val paramObject = JsonObject()
        paramObject.addProperty("position", workingInfoRequest.position)
        paramObject.addProperty("company_name", workingInfoRequest.company)
        paramObject.addProperty("company_country", workingInfoRequest.country)
        paramObject.addProperty("company_address", workingInfoRequest.address)
        paramObject.addProperty("company_phone", workingInfoRequest.telephone)
        paramObject.addProperty("year_start_date", workingInfoRequest.yearStartDate)
        paramObject.addProperty("month_start_date", workingInfoRequest.monthStartDate)
        paramObject.addProperty("year_end_date", workingInfoRequest.yearEndDate)
        paramObject.addProperty("month_end_date", workingInfoRequest.monthEndDate)
        paramObject.addProperty("description", workingInfoRequest.description)
        return paramObject
    }

    private fun candidateTechnicalRoleInfoToJsonObject(technicalRoleInfoRequest: CreateCandidateTechnicalRoleInfoRequest): JsonObject {
        val paramObject = JsonObject()
        paramObject.addProperty("role", technicalRoleInfoRequest.name)
        paramObject.addProperty("experience_years", technicalRoleInfoRequest.experience)
        paramObject.addProperty("description", technicalRoleInfoRequest.description)
        return paramObject
    }

    private fun candidateTechnologyInfoToJsonObject(technologyInfoRequest: CreateCandidateTechnologyInfoRequest): JsonObject {
        val paramObject = JsonObject()
        paramObject.addProperty("name", technologyInfoRequest.name)
        paramObject.addProperty("experience_years", technologyInfoRequest.experience)
        paramObject.addProperty("level", technologyInfoRequest.level)
        paramObject.addProperty("description", technologyInfoRequest.description)
        return paramObject
    }
}