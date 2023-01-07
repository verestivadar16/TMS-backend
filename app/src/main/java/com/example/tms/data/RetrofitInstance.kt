package com.example.tms.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: ImagesApi by lazy {

        Retrofit.Builder()
                .baseUrl("http://192.168.1.5:8100")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ImagesApi::class.java)

    }

}