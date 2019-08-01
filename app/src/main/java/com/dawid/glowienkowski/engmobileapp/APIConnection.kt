package com.dawid.glowienkowski.engmobileapp


import retrofit2.Call
import retrofit2.http.*

interface APIConnection {

    @GET("api/authentication")
    fun loginUser(@Header("Authorization") header: String): Call<Void>

    @GET
    fun getTabooCards(@Header("Authorization") header: String,
                      @Url url : String): Call<List<TabooCardModel>>
    @GET
    fun getTabooCard(@Header("Authorization") header: String,
                      @Url url : String): Call<TabooCardModel>

    @GET
    fun getHeadsUpCards(@Header("Authorization") header: String,
                      @Url url : String): Call<List<HeadsUpCardModel>>
    @GET
    fun getHeadsUpCard(@Header("Authorization") header: String,
                     @Url url : String): Call<HeadsUpCardModel>
}