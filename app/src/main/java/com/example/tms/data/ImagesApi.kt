package com.example.tms.data

import retrofit2.Response
import retrofit2.http.GET

interface ImagesApi {

    @GET("/images")
    suspend fun getImages(): Response<Image>

//    companion object{
//        const val BASE_URL = "http://127.0.0.1:8100"
//    }
}

