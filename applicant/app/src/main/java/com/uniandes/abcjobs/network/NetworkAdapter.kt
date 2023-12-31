package com.uniandes.abcjobs.network

import com.google.gson.JsonObject
import com.uniandes.abcjobs.models.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

object NetworkAdapter {

    private val candidateResource: CandidatesResource = RetrofitHelper.getRetrofit().create(CandidatesResource::class.java)
    private val loginResource: LoginResource = RetrofitHelper.getRetrofit().create(LoginResource::class.java)
    suspend fun getCandidates(): List<Candidate> = candidateResource.getCandidates()

    suspend fun getInterviews(candidateId: Int): List<Interview> = candidateResource.getInterviews(candidateId)

    suspend fun getCandidate(candidateId: Int): Candidate = candidateResource.getCandidate(candidateId)

    suspend fun createCandidate(candidate : JsonObject): JsonObject = candidateResource.createCandidate(candidate)

    suspend fun createCandidateAcademicInfo(candidateAcademicInfo : JsonObject, bearerToken : String): CreateAcademicInfoResponse = candidateResource.createCandidateAcademicInfo(candidateAcademicInfo, bearerToken)

    suspend fun createCandidateWorkingInfo(candidateWorkingInfo : JsonObject, bearerToken : String): CreateAcademicInfoResponse = candidateResource.createCandidateWorkingInfo(candidateWorkingInfo, bearerToken)

    suspend fun createCandidateTechnicalRoleInfo(candidateTechnicalRoleInfo : JsonObject, bearerToken : String): CreateAcademicInfoResponse = candidateResource.createCandidateTechnicalRoleInfo(candidateTechnicalRoleInfo, bearerToken)

    suspend fun createCandidateTechnologyInfo(candidateTechnologyInfo : JsonObject, bearerToken : String): CreateAcademicInfoResponse = candidateResource.createCandidateTechnologyInfo(candidateTechnologyInfo, bearerToken)

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

    @GET("/interviews")
    suspend fun getInterviews(@Query("candidate") candidateID: Int): List<Interview>

    @GET("/candidates/{id}")
    suspend fun getCandidate(@Path("id") candidateID: Int): Candidate

    @POST("/candidates")
    suspend fun createCandidate(@Body candidate: JsonObject): JsonObject

    @POST("/candidates/myself/academic_info")
    suspend fun createCandidateAcademicInfo(@Body academicInfo: JsonObject, @Header("Authorization") bearerToken : String): CreateAcademicInfoResponse

    @POST("/candidates/myself/laboral_info")
    suspend fun createCandidateWorkingInfo(@Body workingInfo: JsonObject, @Header("Authorization") bearerToken : String): CreateAcademicInfoResponse

    @POST("/candidates/myself/technical_roles")
    suspend fun createCandidateTechnicalRoleInfo(@Body technicalRoleInfo: JsonObject, @Header("Authorization") bearerToken : String): CreateAcademicInfoResponse

    @POST("/candidates/myself/technologies")
    suspend fun createCandidateTechnologyInfo(@Body technologyInfo: JsonObject, @Header("Authorization") bearerToken : String): CreateAcademicInfoResponse

}

interface LoginResource {

    @POST("/user/login")
    suspend fun login(@Body login: JsonObject): LoginResponse

    @GET("/user/myself")
    suspend fun whoIAm(@Header("Authorization") bearerToken: String): MyselfResponse



}