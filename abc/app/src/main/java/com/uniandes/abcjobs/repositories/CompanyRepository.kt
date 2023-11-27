package com.uniandes.abcjobs.repositories

import android.util.Log
import com.google.gson.JsonArray
import com.uniandes.abcjobs.network.CacheManager
import com.uniandes.abcjobs.network.NetworkAdapter
import com.uniandes.abcjobs.models.*

class CompanyRepository (){

    suspend fun getCompanies(onComplete:(resp:List<Company>)->Unit, onError: (error: Exception)->Unit) {
        var potentialResp = CacheManager.getInstance().get("companies", Test::class.java)
        try {
        if(potentialResp==null){
            Log.i("Cache", "from network")
                var enabledTests = NetworkAdapter.getCompanies()

            if (enabledTests != null) {
                CacheManager.getInstance().put("tests", enabledTests, Test::class.java)
            }
            onComplete(enabledTests)


        } else{
            var result = potentialResp as List<Company>
            Log.i("Cache", "return tests from cache")
            onComplete(result)
        }
        }catch (e:Exception){
            onError(e)
        }
    }

}

