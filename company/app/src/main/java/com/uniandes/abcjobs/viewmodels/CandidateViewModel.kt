package com.uniandes.abcjobs.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.uniandes.abcjobs.repositories.CandidateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.gson.JsonObject
import com.uniandes.abcjobs.models.*
import com.uniandes.abcjobs.repositories.LoginRepository

class CandidateViewModel(application: Application) :  AndroidViewModel(application) {

    private val candidatesRepo = CandidateRepository()

    private val loginRepository = LoginRepository()

    private val candidatesMutableData = MutableLiveData<List<Candidate>>()
    private val candidatesSearchMutableData = MutableLiveData<List<CandidateResponseSearch>>()

    private var _eventLoginFail = MutableLiveData<Boolean>(false)
    val eventLoginFail: LiveData<Boolean>
        get() = _eventLoginFail

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

    private var _eventGetCandidateSuccess = MutableLiveData<Boolean>(false)
    val eventGetCandidateSuccess: LiveData<Boolean>
        get() = _eventGetCandidateSuccess

    private var _isUnSuccessShownCandidatesInfo = MutableLiveData<Boolean>(false)
    val isUnSuccessShownCandidatesInfo: LiveData<Boolean>
        get() = _isUnSuccessShownCandidatesInfo

    val candidatesSearch: LiveData<List<CandidateResponseSearch>>
        get() = candidatesSearchMutableData

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

    fun searchCandidate(search_filter: CandidateRequestSearch, onComplete:(resp: List<CandidateResponseSearch>)->Unit,
                                onError: (error: Exception)->Unit) {
                println("Enviando peticion desde el viewmodel para buscar")
                viewModelScope.launch(Dispatchers.Default) {
                    withContext(Dispatchers.IO) {
                        loginRepository.whoIAm({ response ->
                            var token = response.token
                            viewModelScope.launch(Dispatchers.Default) {
                                withContext(Dispatchers.IO) {
                                    if (token != null) {
                                        candidatesRepo.searchCandidate(token, search_filter,
                                            {
                                                //it.size?.let { it1 -> Log.d("Success", it1.toString()) }
                                                _eventGetCandidateSuccess.postValue(true)
                                                _isUnSuccessShownCandidatesInfo.postValue(false)
                                                onComplete(it)
                                            },
                                            {
                                                onError(it)
                                            })
                                    }
                                }
                            }
                        }, {
                            //handleException(it)
                            onError(it)
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
}