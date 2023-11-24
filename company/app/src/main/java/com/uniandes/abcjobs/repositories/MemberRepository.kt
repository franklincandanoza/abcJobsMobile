package com.uniandes.abcjobs.repositories

import android.util.Log
import com.google.gson.JsonObject
import com.uniandes.abcjobs.models.*
import com.uniandes.abcjobs.network.CacheManager
import com.uniandes.abcjobs.network.NetworkAdapter

class MemberRepository {
    suspend fun getMembersByProject(token: String, project_id: Int, onComplete:(resp: List<ProjectMemberResponse>)->Unit,
                                     onError: (error: Exception)->Unit) {
        println("Enviando peticion de miembros desde el repositorio")

        try{
            var members = NetworkAdapter.getMembersByProject("Bearer $token", project_id)
            //CacheManager.getInstance().put("", projects, ProjectResponse::class.java)
            onComplete(members)
        }
        catch (e:Exception){
            Log.i("Error", "${e} ")
            onError(e)
        }

    }
}