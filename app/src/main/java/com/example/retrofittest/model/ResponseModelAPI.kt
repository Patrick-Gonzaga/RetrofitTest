package com.example.retrofittest.model

data class ResponseModelAPI(
    val data: Data,
    val status: Int
)

data class Data(
    val mal_id: Int,
    val name: String?,
    val name_kanji: String?,
    val favorites: Int?,
    val images: Images?,
    val anime: List<CharAnime>?
)

data class Images(
    val jpg: Jpg?
)

data class Jpg(
    val image_url: String?
)

data class CharAnime(
    val anime: AnimeInfo?
)

data class AnimeInfo(
    val title: String?
)