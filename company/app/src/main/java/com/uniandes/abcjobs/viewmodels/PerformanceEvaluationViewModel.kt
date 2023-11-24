package com.uniandes.abcjobs.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.uniandes.abcjobs.models.*
import com.uniandes.abcjobs.repositories.CandidateRepository
import com.uniandes.abcjobs.repositories.LoginRepository
import com.uniandes.abcjobs.repositories.ProjectRepository
import com.uniandes.abcjobs.repositories.PerformanceEvaluationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerformanceEvaluationViewModel(application: Application) :  AndroidViewModel(application) {

    private val projectsRepo = ProjectRepository()

    private val loginRepository = LoginRepository()

    private val evaluationRepository = PerformanceEvaluationRepository()

    private val evaluationsMutableData = MutableLiveData<List<PerformanceEvaluationRequest>>()

    private var _eventLoginFail = MutableLiveData<Boolean>(false)
    val eventLoginFail: LiveData<Boolean>
        get() = _eventLoginFail

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError


    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private var _createEvaluationSuccess = MutableLiveData<Boolean>(false)
    val createEvaluationSuccess: LiveData<Boolean>
        get() = _createEvaluationSuccess

    private var _isUnSuccessCreateEvaluation = MutableLiveData<Boolean>(false)
    val isUnSuccessCreateEvaluation: LiveData<Boolean>
        get() = _isUnSuccessCreateEvaluation


    fun onUnSuccessSCreateEvaluation() {
        _isUnSuccessCreateEvaluation.value = true
    }
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    fun createEvaluation(request : PerformanceEvaluationRequest, onComplete:(resp: JsonObject)->Unit,
                  onError: (error: Exception)->Unit){
        println("Enviando peticion de evaluacion desde el viewmodel")
        viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.IO) {
                loginRepository.whoIAm({ response ->
                    var token = response.token
                    viewModelScope.launch(Dispatchers.Default) {
                        withContext(Dispatchers.IO) {
                            if (token != null) {
                                evaluationRepository.createEvaluation(token, evaluationToJsonObject(request),
                                    {
                                        //it.size?.let { it1 -> Log.d("Success", it1.toString()) }
                                        _createEvaluationSuccess.postValue(true)
                                        _isUnSuccessCreateEvaluation.postValue(false)
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

    private fun evaluationToJsonObject(evaluation: PerformanceEvaluationRequest): JsonObject {
        val paramObject = JsonObject()
        paramObject.addProperty("score", evaluation.score)
        paramObject.addProperty("details", evaluation.details)
        paramObject.addProperty("project_id", evaluation.project_id)
        paramObject.addProperty("person_id", evaluation.person_id)
        paramObject.addProperty("member_id", evaluation.member_id)
        return paramObject
    }
}