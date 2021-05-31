package com.example.loginkotlin

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("/webservice/api_loginkotlin.php")
    fun login(@Field("funcion") function: String,
              @Field("email") email: String,
              @Field("password") password: String) : Call<String>
}