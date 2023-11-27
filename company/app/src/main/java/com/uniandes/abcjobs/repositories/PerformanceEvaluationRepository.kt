package com.uniandes.abcjobs.repositories

import android.util.Log
import com.google.gson.JsonObject
import com.uniandes.abcjobs.models.*
import com.uniandes.abcjobs.network.CacheManager
import com.uniandes.abcjobs.network.NetworkAdapter
import com.uniandes.abcjobs.network.ProjectsResource

class PerformanceEvaluationRepository {

    suspend fun createEvaluation(token: String, evaluation: JsonObject, onComplete:(resp: JsonObject)->Unit,
                          onError: (error: Exception)->Unit) {
        println("Enviando peticion de crear evaluacion desde el repositorio")
        try{
            var response = NetworkAdapter.createEvaluation("Bearer $token", evaluation)
            //CacheManager.getInstance().put("", projects, ProjectResponse::class.java)
            onComplete(response)
        }
        catch (e:Exception){
            Log.i("Error", "${e} ")
            onError(e)
        }
    }
}