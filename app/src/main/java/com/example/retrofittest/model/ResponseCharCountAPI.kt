package com.example.retrofittest.model

data class ResponseCharCountAPI(
    val pagination: Pagination
)

data class Pagination(
    val items: Items
)

data class Items(
    val total: Int
)
