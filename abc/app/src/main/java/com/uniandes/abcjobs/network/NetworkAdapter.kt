package com.uniandes.abcjobs.network


import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.uniandes.abcjobs.models.*
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit


object NetworkAdapter {

    private val candidateResource: CandidatesResource =
        RetrofitHelper.getRetrofit().create(CandidatesResource::class.java)
    private val loginResource: LoginResource =
        RetrofitHelper.getRetrofit().create(LoginResource::class.java)
    private val testResource: TestResource =
        RetrofitHelper.getRetrofit().create(TestResource::class.java)

    suspend fun getCandidates(): List<CandidateItem> = candidateResource.getCandidates()

    suspend fun getCandidate(candidateId: String): List<CandidateItem> =
        candidateResource.getCandidates(candidateId)

    suspend fun createCandidate(candidate: JsonObject): CandidateResponse =
        candidateResource.createCandidate(candidate)

    suspend fun login(login: JsonObject): LoginResponse = loginResource.login(login)


    suspend fun whoIAm(bearerToken: String): MyselfResponse = loginResource.whoIAm(bearerToken)

    suspend fun registerTestResult(registerResult: JsonArray): RegisterResultTestResponse =
        testResource.registerResult(registerResult)

    suspend fun getEnabledTests(): List<Test> = testResource.getEnabledTests()

    /*{
        var result = testResource.getEnabledTests()
        if(result.isSuccessful){
            val gson = GsonBuilder().create()
            return gson.fromJson(result.body(), Array<Test>::class.java).toList()
        }
        throw RuntimeException(result.errorBody().toString())
    }*/


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

                //.addCallAdapterFactory(DefaultCallAdapterFactory)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
    }

    interface CandidatesResource {

        @GET("/candidates")
        suspend fun getCandidates(@Query("documents") candidateID: String?=null): List<CandidateItem>

        @POST("/candidates")
        suspend fun createCandidate(@Body candidate: JsonObject): CandidateResponse

    }

    interface LoginResource {

        @POST("/user/login")
        suspend fun login(@Body login: JsonObject): LoginResponse

        @GET("/user/myself")
        suspend fun whoIAm(@Header("Authorization") bearerToken: String): MyselfResponse
    }


    interface TestResource {

        @POST("/tests/results")
        suspend fun registerResult(@Body registerTest: JsonArray): RegisterResultTestResponse

        @GET("/enabled_tests")
        suspend fun getEnabledTests(): List<Test>


    }
}