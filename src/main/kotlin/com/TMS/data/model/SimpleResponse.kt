package com.TMS.data.model

@kotlinx.serialization.Serializable
data class SimpleResponse(
    val success:Boolean,
    val message:String
)
