package com.uniandes.abcjobs.repositories

import android.util.Log
import com.google.gson.JsonObject
import com.uniandes.abcjobs.models.*
import com.uniandes.abcjobs.network.CacheManager
import com.uniandes.abcjobs.network.NetworkAdapter
import com.uniandes.abcjobs.network.ProjectsResource

class ProjectRepository (){
    suspend fun refreshData(onComplete:(resp:List<ProjectResponse>)->Unit, onError: (error: Exception)->Unit) {

        var potentialResp = CacheManager.getInstance().get("", ProjectResponse::class.java)
        return if(potentialResp==null){
            Log.i("Cache", "from network")
            try {
                var projects = NetworkAdapter.getProjects()
                CacheManager.getInstance().put("", projects, ProjectResponse::class.java)
                onComplete(projects)
            }catch (e:Exception){
                onError(e)
            }
        } else{
            var result = potentialResp as List<ProjectResponse>
            Log.i("Cache", "return ${result.size} elements from cache")
            onComplete(result)
        }
    }

    suspend fun getProjectByCompany(token: String, onComplete:(resp: List<ProjectResponse>)->Unit,
                                    onError: (error: Exception)->Unit) {
        println("Enviando peticion desde el repositorio")

        try{
            var projects = NetworkAdapter.getProjectsByCompany("Bearer $token")
            //CacheManager.getInstance().put("", projects, ProjectResponse::class.java)
            onComplete(projects)
        }
        catch (e:Exception){
            Log.i("Error", "${e} ")
            onError(e)
        }

    }

    suspend fun getProfilesByProject(token: String, project_id: Int, onComplete:(resp: List<ProfileResponse>)->Unit,
                                     onError: (error: Exception)->Unit) {
        println("Enviando peticion de perfiles desde el repositorio")

        try{
            var profiles = NetworkAdapter.getProfilesByProject("Bearer $token", project_id)
            //CacheManager.getInstance().put("", projects, ProjectResponse::class.java)
            onComplete(profiles)
        }
        catch (e:Exception){
            Log.i("Error", "${e} ")
            onError(e)
        }

    }
}