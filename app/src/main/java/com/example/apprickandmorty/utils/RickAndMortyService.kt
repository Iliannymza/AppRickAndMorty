package com.example.superheroe.utils

import com.example.superheroe.data.Superheroe
import com.example.superheroe.data.SuperheroeSearchResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface RickAndMortyService {

    @GET("search/{name}")
    suspend fun findSuperheroesByName(@Path("name") name: String): SuperheroeSearchResponse

    @GET("{charcter-id}")
    suspend fun findSuperheroById(@Path("charcter-id") id: String): Superheroe

    companion object {
        fun getInstance(): RickAndMortyService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://superheroapi.com/api/13099c4df9a70d72397e10f0f715d8d7/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(RickAndMortyService::class.java)
        }
    }
}


