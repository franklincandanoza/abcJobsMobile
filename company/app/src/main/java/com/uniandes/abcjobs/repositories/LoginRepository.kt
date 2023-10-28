package com.uniandes.abcjobs.repositories

import android.util.Log
import com.uniandes.abcjobs.network.CacheManager
import com.uniandes.abcjobs.network.NetworkAdapter
import com.google.gson.JsonObject
import com.uniandes.abcjobs.models.*

class LoginRepository (){
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

