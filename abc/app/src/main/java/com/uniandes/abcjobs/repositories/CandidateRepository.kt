package com.uniandes.abcjobs.repositories

import android.util.Log
import com.uniandes.abcjobs.network.CacheManager
import com.uniandes.abcjobs.network.NetworkAdapter
import com.google.gson.JsonObject
import com.uniandes.abcjobs.models.*

class CandidateRepository (){
    suspend fun refreshData(onComplete:(resp:List<CandidateItem>)->Unit, onError: (error: Exception)->Unit) {

        var potentialResp = CacheManager.getInstance().get("", CandidateItem::class.java)
        return if(potentialResp==null){
            Log.i("Cache", "from network")
            try {
                var candidates = NetworkAdapter.getCandidates()
                CacheManager.getInstance().put("", candidates, CandidateItem::class.java)
                onComplete(candidates)
            }catch (e:Exception){
                onError(e)
            }
        } else{
            var result = potentialResp as List<CandidateItem>
            Log.i("Cache", "return ${result.size} elements from cache")
            onComplete(result)
        }
    }

    suspend fun getCandidate(candidateId:String, onComplete:(resp: CandidateItem?)->Unit, onError: (error: Exception)->Unit) {
        var potentialResp = CacheManager.getInstance().get(candidateId, CandidateItem::class.java)

        if(potentialResp==null){
            Log.i("Cache", "from network")
            try {
                var candidate = NetworkAdapter.getCandidate(candidateId)

                CacheManager.getInstance().put(candidateId, candidate, CandidateItem::class.java)
                if(candidate.size==1){
                    onComplete(candidate[0])
                }else{
                    onComplete(null)
                }fo

            }catch (e:Exception){
                onError(e)
            }

        } else{
            var result = potentialResp as List<CandidateItem>
            Log.i("Cache", "return element from cache")
            if(result.size==1){
                onComplete(result[0])
            }else{
                onComplete(null)
            }
        }
    }

    suspend fun createCandidate(candidate: JsonObject): CandidateResponse {
        CacheManager.getInstance().invalidate("", Candidate::class.java)
        return NetworkAdapter.createCandidate(candidate)
    }
    /* ---- Metodos crear adjuntos de candidato--*/
}

