package com.example.superheroe.utils

import com.example.apprickandmorty.data.CharacterResponse
import com.example.apprickandmorty.data.EpisodeResponse
import com.google.android.gms.awareness.snapshot.LocationResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyService {
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int = 1): CharacterResponse

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): Character

    @GET("location")
    fun getLocations(@Query("page") page: Int? = 1): LocationResponse

    @GET("episode")
    fun getEpisodes(@Query("page") page: Int? = 1): EpisodeResponse

    companion object {
        fun getInstance(): RickAndMortyService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://rickandmortyapi.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(RickAndMortyService::class.java)
        }
    }
}


