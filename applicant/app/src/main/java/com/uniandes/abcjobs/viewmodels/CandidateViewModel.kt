package com.uniandes.abcjobs.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.uniandes.abcjobs.models.Candidate
import com.uniandes.abcjobs.models.CandidateRequest
import com.uniandes.abcjobs.models.CreateAcademicInfoRequest
import com.uniandes.abcjobs.repositories.CandidateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.gson.JsonObject
import com.uniandes.abcjobs.models.CandidateResponse
import com.uniandes.abcjobs.repositories.LoginRepository

class CandidateViewModel(application: Application) :  AndroidViewModel(application) {

    private val candidatesRepo = CandidateRepository()

    private val loginRepository = LoginRepository()

    private val candidatesMutableData = MutableLiveData<List<Candidate>>()

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

    //val candidates: LiveData<List<Candidate>>
      //  get() = candidatesMutableData

    init {
        //refreshCandidates()
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
                loginRepository.whoIAm({
                    var token = it.token
                    viewModelScope.launch (Dispatchers.Default){
                        withContext(Dispatchers.IO) {
                            if (token != null) {
                                candidatesRepo.createCandidateAcademicInfo(candidateAcademicInfoToJsonObject(request),token,
                                    {
                                        it.msg?.let { it1 -> Log.d("Success", it1) }

                                        //LoginResponseMutableData.postValue(it)
                                        _eventCreationSuccess.postValue(true)
                                        _isSuccessShownToCreateAcademicInfo.postValue(false)
                                    },
                                    {
                                        Log.d("Error", it.toString())
                                        if (it is retrofit2.HttpException){
                                            val err = it as retrofit2.HttpException
                                            Log.d("Error Response", err.response()?.code().toString())
                                            if (err.response()?.code()==401){
                                                Log.d("Error Response", "response code 401 trying to create academic info")
                                                _eventLoginFail.postValue(true)
                                                _isUnSuccessShownToCreateAcademicInfo.postValue(false)
                                            }else{
                                                Log.d("Error Response", "response code else")
                                                _eventNetworkError.postValue(true)
                                                _isNetworkErrorShown.postValue(false)
                                            }
                                        }else{
                                            _eventNetworkError.postValue(true)
                                            _isNetworkErrorShown.postValue(false)
                                        }

                                    }
                                )
                            }
                        }
                    }
                },{
                    Log.d("Error", it.toString())
                    if (it is retrofit2.HttpException){
                        val err = it as retrofit2.HttpException
                        Log.d("Error Response", err.response()?.code().toString())
                        if (err.response()?.code()==401){
                            Log.d("Error Response", "response code 401 trying to create academic info")
                            _eventLoginFail.postValue(true)
                            _isUnSuccessShownToCreateAcademicInfo.postValue(true)
                        }else{
                            Log.d("Error Response", "response code else")
                            _eventNetworkError.postValue(true)
                            _isNetworkErrorShown.postValue(false)
                        }
                    }else{
                        _eventNetworkError.postValue(true)
                        _isNetworkErrorShown.postValue(false)
                    }
                })
            }
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
}