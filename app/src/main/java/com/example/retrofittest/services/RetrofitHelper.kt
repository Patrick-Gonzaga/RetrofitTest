package com.example.retrofittest.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    companion object{
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.jikan.moe/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val urlDefaultImage = "https://cdn.myanimelist.net/img/sp/icon/apple-touch-icon-256.png"
    }
}