package com.uniandes.abcjobs.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkAdapter {

}

object RetrofitHelper {
    fun getRetrofit(): Retrofit {

        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://vinyls-team4.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}
