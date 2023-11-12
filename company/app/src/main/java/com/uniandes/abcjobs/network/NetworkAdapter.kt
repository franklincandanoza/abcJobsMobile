package com.uniandes.abcjobs.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.google.gson.JsonObject
import com.uniandes.abcjobs.models.*
import retrofit2.http.*

import java.util.concurrent.TimeUnit

object NetworkAdapter {

    private val candidateResource: CandidatesResource = RetrofitHelper.getRetrofit().create(CandidatesResource::class.java)
    private val loginResource: LoginResource = RetrofitHelper.getRetrofit().create(LoginResource::class.java)
    private val projectResource: ProjectsResource = RetrofitHelper.getRetrofit().create(ProjectsResource::class.java)
    private val technologyResource: TechnologiesResource = RetrofitHelper.getRetrofit().create(TechnologiesResource::class.java)

    suspend fun getCandidates(): List<Candidate> = candidateResource.getCandidates()

    suspend fun getCandidate(candidateId: Int): Candidate = candidateResource.getCandidate(candidateId)

    suspend fun createCandidate(candidate : JsonObject): CandidateResponse = candidateResource.createCandidate(candidate)

    suspend fun searchCandidate(bearerToken: String, roleFilter: String, role: String, roleExperience: String, technologies: String, abilities: String,
    titleFilter: String, title: String, titleExperience: String): List<CandidateResponseSearch> = candidateResource.searchCandidate(bearerToken, roleFilter, role, roleExperience, technologies, abilities,
        titleFilter, title, titleExperience)

    suspend fun getProjects(): List<ProjectResponse> =  projectResource.getProjects()

    suspend fun getProjectsByCompany(bearerToken : String): List<ProjectResponse> =  projectResource.getProjectsByCompany(bearerToken)

    suspend fun getProfilesByProject(bearerToken: String, projectId :Int): List<ProfileResponse> = projectResource.getProfilesByProject(bearerToken, projectId)

    suspend fun getTechnologies(bearerToken : String): List<TechnologyResponse> =  technologyResource.getTechnologies(bearerToken)

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

    @GET("/candidates/search")
    suspend fun searchCandidate(@Header("Authorization") bearerToken: String, @Query("roleFilter") roleFilter: String ,
                                @Query("role") role:String ,  @Query("roleExperience") roleExperience: String ,
                                @Query("technologies") technologies:String , @Query("abilities")  abilities:String ,
                                @Query("titleFilter") titleFilter:String , @Query("title") title:String ,
                                @Query("titleExperience") titleExperience:String ): List<CandidateResponseSearch>

}

interface LoginResource {

    @POST("/user/login")
    suspend fun login(@Body login: JsonObject): LoginResponse

    @GET("/user/myself")
    suspend fun whoIAm(@Header("Authorization") bearerToken: String): MyselfResponse
}

interface ProjectsResource {
    @GET("/projects")
    suspend fun getProjects(): List<ProjectResponse>

    @GET("/projects/myself")
    suspend fun getProjectsByCompany(@Header("Authorization") bearerToken: String): List<ProjectResponse>

    @GET("/projects/profiles/{project_id}")
    suspend fun getProfilesByProject(@Header("Authorization") bearerToken: String, @Path("project_id") projectId:Int): List<ProfileResponse>
}

interface TechnologiesResource {
    @GET("/technologies")
    suspend fun getTechnologies(@Header("Authorization") bearerToken: String): List<TechnologyResponse>
}