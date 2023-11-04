package com.uniandes.abcjobs.repositories

import android.util.Log
import com.google.gson.JsonObject
import com.uniandes.abcjobs.models.Candidate
import com.uniandes.abcjobs.models.CandidateResponse
import com.uniandes.abcjobs.models.CreateAcademicInfoResponse
import com.uniandes.abcjobs.network.CacheManager
import com.uniandes.abcjobs.network.NetworkAdapter

class CandidateRepository (){

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

    suspend fun createCandidate(candidate: JsonObject): CandidateResponse {
        CacheManager.getInstance().invalidate("", Candidate::class.java)
        return NetworkAdapter.createCandidate(candidate)
    }

    suspend fun createCandidateAcademicInfo(createCandidateAcademicInfo: JsonObject, token: String,
                                            onComplete:(resp: CreateAcademicInfoResponse)->Unit,
                                            onError: (error: Exception)->Unit) {

        try{

            Log.i("createCandidateAcademic", "${createCandidateAcademicInfo} ")
            var response = NetworkAdapter.createCandidateAcademicInfo(createCandidateAcademicInfo,
                "Bearer $token"
            )
            onComplete(response)
        }catch (e:Exception){
            Log.i("Error", "${e} ")
            onError(e)
        }
    }

    /* ---- Metodos crear adjuntos de candidato--*/
}

