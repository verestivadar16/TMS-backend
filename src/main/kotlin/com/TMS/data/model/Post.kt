package com.TMS.data.model

@kotlinx.serialization.Serializable
data class Post(
    val id:String,
    val title: String,
    val description: String,
    val date: Long
)

