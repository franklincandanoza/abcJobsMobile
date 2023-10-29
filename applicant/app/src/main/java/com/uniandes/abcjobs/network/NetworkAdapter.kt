package com.uniandes.abcjobs.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.Header

import com.google.gson.JsonObject
import com.uniandes.abcjobs.models.*

import java.util.concurrent.TimeUnit

object NetworkAdapter {

    private val candidateResource: CandidatesResource = RetrofitHelper.getRetrofit().create(CandidatesResource::class.java)
    private val loginResource: LoginResource = RetrofitHelper.getRetrofit().create(LoginResource::class.java)
    suspend fun getCandidates(): List<Candidate> = candidateResource.getCandidates()

    suspend fun getCandidate(candidateId: Int): Candidate = candidateResource.getCandidate(candidateId)

    suspend fun createCandidate(candidate : JsonObject): CandidateResponse = candidateResource.createCandidate(candidate)

    suspend fun createCandidateAcademicInfo(candidateAcademicInfo : JsonObject, bearerToken : String): CreateAcademicInfoResponse = candidateResource.createCandidateAcademicInfo(candidateAcademicInfo, bearerToken)

    suspend fun login(login : JsonObject): LoginResponse = loginResource.login(login)


    suspend fun whoIAm(bearerToken : String): MyselfResponse = loginResource.whoIAm(bearerToken)
}

object RetrofitHelper {
    fun getRetrofit(): Retrofit {

        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS)
            .build()

        //.baseUrl("https://backend-test-dw7pbvtayq-uc.a.run.app")
        return Retrofit.Builder()
            .baseUrl("https://backend-test-dw7pbvtayq-uc.a.run.app")
            //.baseUrl("http://localhost:8000")
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

    @POST("/candidates/myself/academic_info")
    suspend fun createCandidateAcademicInfo(@Body academicInfo: JsonObject, @Header("Authorization") bearerToken : String): CreateAcademicInfoResponse

}

interface LoginResource {

    @POST("/user/login")
    suspend fun login(@Body login: JsonObject): LoginResponse

    @GET("/user/myself")
    suspend fun whoIAm(@Header("Authorization") bearerToken: String): MyselfResponse



}