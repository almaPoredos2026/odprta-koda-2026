package com.example.projektna_naloga.network

import com.example.projektna_naloga.model.QuoteResponse
import retrofit2.http.GET

interface QuoteApi {

    @GET("api/random")
    suspend fun getQuote(): List<QuoteResponse>

}