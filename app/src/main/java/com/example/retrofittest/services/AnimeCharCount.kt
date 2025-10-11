package com.example.retrofittest.services

import com.example.retrofittest.model.ResponseCharCountAPI
import retrofit2.Response
import retrofit2.http.GET

interface AnimeCharCount {
    @GET("characters")
    suspend fun getAnimeCharCount() : Response<ResponseCharCountAPI>
}