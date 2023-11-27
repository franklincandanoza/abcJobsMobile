package com.uniandes.abcjobs.repositories

import android.util.Log
import com.google.gson.JsonArray
import com.uniandes.abcjobs.network.CacheManager
import com.uniandes.abcjobs.network.NetworkAdapter
import com.uniandes.abcjobs.models.*

class InterviewRepository (){

    suspend fun getResultsByProject(projectId: String, onComplete:(resp:List<InterviewResult>)->Unit, onError: (error: Exception)->Unit) {
        var potentialResp = CacheManager.getInstance().get("projectId$projectId", InterviewResult::class.java)
        try {
        if(potentialResp==null){
            Log.i("Cache", "from network")
                var results = NetworkAdapter.getInterviewResultsByProject(projectId)
            Log.i("Interview", "Resultado del repositorio:"+results)

            if (results != null) {
                CacheManager.getInstance().put("projectId$projectId", results, InterviewResult::class.java)
            }
            onComplete(results)


        } else{
            var result = potentialResp as List<InterviewResult>
            Log.i("Cache", "return tests from cache")
            onComplete(result)
        }
        }catch (e:Exception){
            onError(e)
        }
    }

}

