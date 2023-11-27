package com.uniandes.abcjobs.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.uniandes.abcjobs.models.Candidate
import com.uniandes.abcjobs.models.RegisterResultTestRequest
import com.uniandes.abcjobs.models.Test
import com.uniandes.abcjobs.repositories.TestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterTestResultViewModel(application: Application) :  AndroidViewModel(application) {

    private val testRepo = TestRepository()

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

    private val testsMutableData = MutableLiveData<List<Test>>()
    val tests: LiveData<List<Test>>
        get() = testsMutableData


    fun enabledTest(){
        viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.IO) {
                testRepo.getEnabledTests({
                    Log.d("Info", it.toString())
                    testsMutableData.postValue(it)
                },{
                    it.printStackTrace()
                Log.d("Error", it.toString())
            })
            }
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