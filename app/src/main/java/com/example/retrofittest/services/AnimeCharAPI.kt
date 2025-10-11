package com.example.retrofittest.services

import com.example.retrofittest.model.ResponseModelAPI
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AnimeCharAPI {
    @GET("characters/{id}/full")
    suspend fun getAnimeChar(
        @Path("id") id: Int
    ): Response<ResponseModelAPI>
}