package com.uniandes.abcjobs.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST

import com.google.gson.JsonObject
import com.uniandes.abcjobs.models.*
import java.util.concurrent.TimeUnit

object NetworkAdapter {

    private val candidateResource: CandidatesResource = RetrofitHelper.getRetrofit().create(CandidatesResource::class.java)
    suspend fun getCandidates(): List<Candidate> = candidateResource.getCandidates()

    suspend fun getCandidate(candidateId: Int): Candidate = candidateResource.getCandidate(candidateId)

    suspend fun createCandidate(candidate : JsonObject): CandidateResponse = candidateResource.createCandidate(candidate)
}

object RetrofitHelper {
    fun getRetrofit(): Retrofit {

        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://backend-test-dw7pbvtayq-uc.a.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}

interface CandidatesResource {
    @GET("/candidates")
    suspend fun getCandidates(): List<Candidate>

    @GET("/candidates/{id}")
    suspend fun getCandidate(@Path("id") candidateID: Int): Candidate

    @POST("/candidates")
    suspend fun createCandidate(@Body candidate: JsonObject): CandidateResponse

}