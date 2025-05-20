package com.example.apprickandmorty.data

data class CharacterResponse(
    val info: Info,
    val results: List<Character>
)

data class Info(
    val count: Int,
    val pages: Int,
    val next: String,
    val prev: String,
)

data class Character(
    val id: Int,
    val name: String,
    val image: String,
)