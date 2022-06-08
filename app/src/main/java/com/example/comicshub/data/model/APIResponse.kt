package com.example.comicshub.data.model

import com.google.gson.annotations.SerializedName

data class APIResponse(
    @SerializedName("alt")
    val alt: String,
    @SerializedName("day")
    val day: String,
    @SerializedName("img")
    val img: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("month")
    val month: String,
    @SerializedName("news")
    val news: String,
    @SerializedName("num")
    val num: Int,
    @SerializedName("safe_title")
    val safe_title: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("transcript")
    val transcript: String,
    @SerializedName("year")
    val year: String
)