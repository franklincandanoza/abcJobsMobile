package com.uniandes.abcjobs.repositories

import android.util.Log
import com.google.gson.JsonObject
import com.uniandes.abcjobs.models.*
import com.uniandes.abcjobs.network.CacheManager
import com.uniandes.abcjobs.network.NetworkAdapter
import com.uniandes.abcjobs.network.TechnologiesResource


class TechnologyRepository {
    suspend fun getTechnologies(token: String, onComplete:(resp: List<TechnologyResponse>)->Unit,
                                    onError: (error: Exception)->Unit) {
        println("Enviando peticion desde el repositorio")

        try{
            var technologies = NetworkAdapter.getTechnologies("Bearer $token")
            //CacheManager.getInstance().put("", projects, ProjectResponse::class.java)
            onComplete(technologies)
        }
        catch (e:Exception){
            Log.i("Error", "${e} ")
            onError(e)
        }
    }
}