package com.uniandes.abcjobs.repositories

import android.util.Log
import com.google.gson.JsonArray
import com.uniandes.abcjobs.network.CacheManager
import com.uniandes.abcjobs.network.NetworkAdapter
import com.uniandes.abcjobs.models.*

class ProjectRepository (){

    suspend fun getProjectsByCompany(companyId: String, onComplete:(resp:List<Project>)->Unit, onError: (error: Exception)->Unit) {
        var potentialResp = CacheManager.getInstance().get("projects$companyId", Project::class.java)
        try {
        if(potentialResp==null){
            Log.i("Cache", "from network")
                var projects = NetworkAdapter.getProjectsByCompany(companyId)

            if (projects != null) {
                CacheManager.getInstance().put("projects$companyId", projects, Project::class.java)
            }
            onComplete(projects)


        } else{
            var result = potentialResp as List<Project>
            Log.i("Cache", "return tests from cache")
            onComplete(result)
        }
        }catch (e:Exception){
            onError(e)
        }
    }

}

