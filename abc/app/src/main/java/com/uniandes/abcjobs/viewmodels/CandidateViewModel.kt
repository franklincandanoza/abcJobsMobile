package com.uniandes.abcjobs.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.uniandes.abcjobs.models.Candidate
import com.uniandes.abcjobs.models.CandidateRequest
import com.uniandes.abcjobs.repositories.CandidateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.gson.JsonObject
import com.uniandes.abcjobs.models.CandidateItem
import com.uniandes.abcjobs.models.CandidateResponse
import com.uniandes.abcjobs.models.Test

class CandidateViewModel(application: Application) :  AndroidViewModel(application) {

    private val candidatesRepo = CandidateRepository()

    private val candidatesMutableData = MutableLiveData<List<CandidateItem>>()

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    private var _isNetworkErrorShownForCreateAlbum = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private val candidateData = MutableLiveData<CandidateItem>()
    val candidate: LiveData<CandidateItem>
        get() = candidateData


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

    fun findCandidate(candidateDocument:String){
        viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.IO) {
                candidatesRepo.getCandidate(candidateDocument,{
                    Log.d("Info", it.toString())
                    candidateData.postValue(it)
                },{
                    it.printStackTrace()
                    Log.d("Error", it.toString())
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