package com.uniandes.abcjobs.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.uniandes.abcjobs.models.LoginRequest
import com.uniandes.abcjobs.models.LoginResponse
import com.uniandes.abcjobs.repositories.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) :  AndroidViewModel(application) {

    private val loginRepo = LoginRepository()

    private val LoginResponseMutableData = MutableLiveData<LoginResponse>()

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _eventLoginFail = MutableLiveData<Boolean>(false)
    val eventLoginFail: LiveData<Boolean>
        get() = _eventLoginFail

    private var _eventLoginSuccess = MutableLiveData<Boolean>(false)
    val eventLoginSuccess: LiveData<Boolean>
        get() = _eventLoginSuccess

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private var _isSuccessShown = MutableLiveData<Boolean>(false)

    val isSuccessShown: LiveData<Boolean>
        get() = _isSuccessShown



    private var _isUnSuccessShown = MutableLiveData<Boolean>(false)

    val isUnSuccessShown: LiveData<Boolean>
        get() = _isUnSuccessShown
    init {
        //refreshCandidates()
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }
    fun onSuccessLoginShown() {
        _isSuccessShown.value = true
    }
    fun onUnSuccessLoginShown() {
        _isUnSuccessShown.value = true
    }

    fun login(request : LoginRequest) {
            viewModelScope.launch (Dispatchers.Default){
                withContext(Dispatchers.IO) {
                    loginRepo.login(loginToJsonObject(request),
                        {
                            it.token?.let { it1 -> Log.d("Success", it1) }

                            //LoginResponseMutableData.postValue(it)
                            _eventLoginSuccess.postValue(true)
                            _isSuccessShown.postValue(false)
                        },
                        {
                            Log.d("Error", it.toString())
                            if (it is retrofit2.HttpException){
                               val err = it as retrofit2.HttpException
                                Log.d("Error Response", err.response()?.code().toString())
                                if (err.response()?.code()==401){
                                    Log.d("Error Response", "response code 401")
                                    _eventLoginFail.postValue(true)
                                    _isUnSuccessShown.postValue(false)
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



    private fun loginToJsonObject(login: LoginRequest): JsonObject {
        val paramObject = JsonObject()
        paramObject.addProperty("username", login.username)
        paramObject.addProperty("password", login.password)
        return paramObject
    }
}