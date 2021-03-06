package com.example.comicshub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
const val TABLE_NAME = "TABLE_NAME"
@Entity(
    tableName = TABLE_NAME
)
data class APIResponse(
    @SerializedName("alt")
    val alt: String?,
    @SerializedName("day")
    val day: String?,
    @SerializedName("img")
    val img: String?,
    @SerializedName("link")
    val link: String?,
    @SerializedName("month")
    val month: String?,
    @SerializedName("news")
    val news: String?,
    @SerializedName("num")
    @PrimaryKey
    val num: Int?,
    @SerializedName("safe_title")
    val safe_title: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("transcript")
    val transcript: String?,
    @SerializedName("year")
    val year: String?
):Serializable