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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectViewModel (application: Application) :  AndroidViewModel(application) {
    val ERROR_RESPONSE_MESSAGE = "ErrorResponse"

    private val projectsRepo = ProjectRepository()

    private val loginRepository = LoginRepository()

    private val projectsMutableData = MutableLiveData<List<ProjectResponse>>()

    private var _eventLoginFail = MutableLiveData<Boolean>(false)
    val eventLoginFail: LiveData<Boolean>
        get() = _eventLoginFail

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError


    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private var _eventGetProjectSuccess = MutableLiveData<Boolean>(false)
    val eventGetProjectSuccess: LiveData<Boolean>
        get() = _eventGetProjectSuccess

    private var _eventGetProfileSuccess = MutableLiveData<Boolean>(false)
    val eventGetProfileSuccess: LiveData<Boolean>
        get() = _eventGetProfileSuccess

    private var _isUnSuccessShownProjectsInfo = MutableLiveData<Boolean>(false)
    val isUnSuccessShownProjectsInfo: LiveData<Boolean>
        get() = _isUnSuccessShownProjectsInfo


    private var _eventAddMemberSuccess = MutableLiveData<Boolean>(false)
    val eventAddMemberSuccess: LiveData<Boolean>
        get() = _eventAddMemberSuccess


    private var _isSuccessShownProjectsInfo = MutableLiveData<Boolean>(false)
    val isSuccessShownProjectsInfo: LiveData<Boolean>
        get() = _isSuccessShownProjectsInfo

    private var _isSuccessShownProfilesInfo = MutableLiveData<Boolean>(false)
    val isSuccessShownProfilesInfo: LiveData<Boolean>
        get() = _isSuccessShownProfilesInfo


    private var _isUnSuccessAddMember = MutableLiveData<Boolean>(false)
    val isUnSuccessAddMember: LiveData<Boolean>
        get() = _isUnSuccessAddMember

    fun onUnSuccessShownProjectsInfo() {
        _isUnSuccessShownProjectsInfo.value = true
    }

    fun onSuccessShownProjectsInfo() {
        _isSuccessShownProjectsInfo.value = true
    }

    fun onUnSuccessAddMember() {
        _isUnSuccessAddMember.value = true
    }
    init {
        //refreshCandidates()
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    private fun refreshProjects() {
        viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.IO) {
                projectsRepo.refreshData({
                    projectsMutableData.postValue(it)
                    _eventNetworkError.postValue(false)
                    _isNetworkErrorShown.postValue(false)
                }, {
                    Log.d("Error", it.toString())
                    _eventNetworkError.postValue(true)
                })
            }
        }
    }

    fun getProjectsbyCompany(onComplete:(resp: List<ProjectResponse>)->Unit,
                             onError: (error: Exception)->Unit){
        println("Enviando peticion desde el viewmodel")
        viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.IO) {
                loginRepository.whoIAm({ response ->
                    var token = response.token
                    viewModelScope.launch(Dispatchers.Default) {
                        withContext(Dispatchers.IO) {
                            if (token != null) {
                                projectsRepo.getProjectByCompany(token,
                                    {
                                        //it.size?.let { it1 -> Log.d("Success", it1.toString()) }
                                        _eventGetProjectSuccess.postValue(true)
                                        _isSuccessShownProjectsInfo.postValue(false)
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

    fun getProfilesbyProject(project_id: Int,onComplete:(resp: List<ProfileResponse>)->Unit,
                             onError: (error: Exception)->Unit){
        println("Enviando peticion de perfil desde el viewmodel")
        viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.IO) {
                loginRepository.whoIAm({ response ->
                    var token = response.token
                    viewModelScope.launch(Dispatchers.Default) {
                        withContext(Dispatchers.IO) {
                            if (token != null) {
                                projectsRepo.getProfilesByProject(token, project_id,
                                    {
                                        //it.size?.let { it1 -> Log.d("Success", it1.toString()) }
                                        _eventGetProfileSuccess.postValue(true)
                                        _isSuccessShownProjectsInfo.postValue(false)
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

    fun addMember(request : ProjectMemberRequest, onComplete:(resp: JsonObject)->Unit,
                  onError: (error: Exception)->Unit){
        println("Enviando peticion de miembro desde el viewmodel")
        viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.IO) {
                loginRepository.whoIAm({ response ->
                    var token = response.token
                    viewModelScope.launch(Dispatchers.Default) {
                        withContext(Dispatchers.IO) {
                            if (token != null) {
                                projectsRepo.addMember(token, memberToJsonObject(request),
                                    {
                                        //it.size?.let { it1 -> Log.d("Success", it1.toString()) }
                                        _eventAddMemberSuccess.postValue(true)
                                        _isUnSuccessAddMember.postValue(false)
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
    private fun handleException(exception: Exception) {
        Log.d("Error", exception.toString())
        if (exception is retrofit2.HttpException) {
            val err = exception as retrofit2.HttpException
            Log.d(ERROR_RESPONSE_MESSAGE, err.response()?.code().toString())
            if (err.response()?.code() == 404) {
                Log.d(ERROR_RESPONSE_MESSAGE, "response code 404 trying to list projects")
                _eventLoginFail.postValue(true)
                _isUnSuccessShownProjectsInfo.postValue(true)
            } else {
                Log.d(ERROR_RESPONSE_MESSAGE, "response code else")
                _eventNetworkError.postValue(true)
                _isNetworkErrorShown.postValue(false)
            }
        } else {
            _eventNetworkError.postValue(true)
            _isNetworkErrorShown.postValue(false)
        }
    }

    private fun memberToJsonObject(member: ProjectMemberRequest): JsonObject {
        val paramObject = JsonObject()
        paramObject.addProperty("active", 1)
        paramObject.addProperty("description", "Nuevo miembro")
        paramObject.addProperty("personId", member.personId)
        paramObject.addProperty("profileId", member.profileId)
        paramObject.addProperty("projectId", member.projectId)
        return paramObject
    }

}