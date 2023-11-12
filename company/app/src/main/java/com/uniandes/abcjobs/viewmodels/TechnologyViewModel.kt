package com.uniandes.abcjobs.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uniandes.abcjobs.models.ProjectResponse
import com.uniandes.abcjobs.models.TechnologyResponse
import com.uniandes.abcjobs.repositories.LoginRepository
import com.uniandes.abcjobs.repositories.ProjectRepository
import com.uniandes.abcjobs.repositories.TechnologyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TechnologyViewModel (application: Application) :  AndroidViewModel(application) {
    private val technologyRepo = TechnologyRepository()

    private val loginRepository = LoginRepository()

    private val technologiesMutableData = MutableLiveData<List<ProjectResponse>>()

    private var _eventLoginFail = MutableLiveData<Boolean>(false)
    val eventLoginFail: LiveData<Boolean>
        get() = _eventLoginFail

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError


    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private var _eventGetTechnologySuccess = MutableLiveData<Boolean>(false)
    val eventGetTechnologySuccess: LiveData<Boolean>
        get() = _eventGetTechnologySuccess

    private var _isUnSuccessShownTechnologiesInfo = MutableLiveData<Boolean>(false)
    val isUnSuccessShownTechnologiesInfo: LiveData<Boolean>
        get() = _isUnSuccessShownTechnologiesInfo

    fun getTechnologies(onComplete:(resp: List<TechnologyResponse>)->Unit,
                             onError: (error: Exception)->Unit){
        println("Enviando peticion desde el viewmodel")
        viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.IO) {
                loginRepository.whoIAm({ response ->
                    var token = response.token
                    viewModelScope.launch(Dispatchers.Default) {
                        withContext(Dispatchers.IO) {
                            if (token != null) {
                                technologyRepo.getTechnologies(token,
                                    {
                                        //it.size?.let { it1 -> Log.d("Success", it1.toString()) }
                                        _eventGetTechnologySuccess.postValue(true)
                                        _isUnSuccessShownTechnologiesInfo.postValue(false)
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

}