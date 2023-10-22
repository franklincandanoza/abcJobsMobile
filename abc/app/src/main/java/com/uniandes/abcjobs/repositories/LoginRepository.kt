package com.uniandes.abcjobs.repositories

import android.util.Log
import com.uniandes.abcjobs.network.CacheManager
import com.uniandes.abcjobs.network.NetworkAdapter
import com.google.gson.JsonObject
import com.uniandes.abcjobs.models.*

class LoginRepository (){
    suspend fun refreshData(onComplete:(resp:List<Candidate>)->Unit, onError: (error: Exception)->Unit) {

        var potentialResp = CacheManager.getInstance().get("", Candidate::class.java)
        return if(potentialResp==null){
            Log.i("Cache", "from network")
            try {
                var candidates = NetworkAdapter.getCandidates()
                CacheManager.getInstance().put("", candidates, Candidate::class.java)
                onComplete(candidates)
            }catch (e:Exception){
                onError(e)
            }
        } else{
            var result = potentialResp as List<Candidate>
            Log.i("Cache", "return ${result.size} elements from cache")
            onComplete(result)
        }
    }

    suspend fun getCandidate(candidateId:Int, onComplete:(resp: Candidate)->Unit, onError: (error: Exception)->Unit) {
        var potentialResp = CacheManager.getInstance().get(candidateId, Candidate::class.java)

        if(potentialResp==null){
            Log.i("Cache", "from network")
            try {
                var candidate = NetworkAdapter.getCandidate(candidateId)

                CacheManager.getInstance().put(candidateId, candidate, Candidate::class.java)
                onComplete(candidate)
            }catch (e:Exception){
                onError(e)
            }

        } else{
            var result = potentialResp as Candidate
            Log.i("Cache", "return element from cache")
            onComplete(result)
        }
    }

    suspend fun login(login: JsonObject,onComplete:(resp: LoginResponse)->Unit, onError: (error: Exception)->Unit) {
        try{
            Log.i("Login", "${login} ")
            var loginResponse = NetworkAdapter.login(login)
            loginResponse.token?.let {
                Log.i("Cache", "Saving ${it} in cache")
                CacheManager.getInstance().put("token",
                    it,String::class.java)
                Log.i("Cache", "Saved in cache")
            }
            onComplete(loginResponse)
        }catch (e:Exception){
            Log.i("Error", "${e} ")
            onError(e)
        }
    }

    suspend fun whoIAm(onComplete:(resp: MyselfResponse)->Unit, onError: (error: Exception)->Unit) {
        try{
            var token = CacheManager.getInstance().get("token", String::class.java)
            var myselfResponse = NetworkAdapter.whoIAm("Bearer "+token);
            myselfResponse.token?.let {
                CacheManager.getInstance().put("token",
                    it,String::class.java)
            }
            onComplete(myselfResponse)
        }catch (e:Exception){
            onError(e)
        }
    }
}

