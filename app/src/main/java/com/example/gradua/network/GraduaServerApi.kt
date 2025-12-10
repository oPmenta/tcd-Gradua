package com.example.gradua.network

import com.example.gradua.data.QuestionJson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

// --- CORREÇÃO IMPORTANTE ---
// O Retrofit EXIGE que a URL termine com uma barra "/".
// Antes: "https://gradua.pythonanywhere.com" (Isso faz o app crashar)
// Agora: "https://gradua.pythonanywhere.com/" (Isso funciona)
private const val BASE_URL = "https://gradua.pythonanywhere.com/"

private const val TOKEN = "d9e8f7a6b5c4d3e2f1a0b9c8d7e6f5a4b3c2d1e0f9a8b7c6d5e4f3a2b1c0d9e8"

interface GraduaApiService {
    @GET("api/questoes") // Retirei a barra do início aqui, pois já tem na BASE_URL
    suspend fun getAllQuestions(
        @Header("Authorization") token: String = "Bearer $TOKEN",
        @Query("materia") materia: String? = null
    ): List<QuestionJson>
}

object GraduaServerApi {
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val service: GraduaApiService by lazy {
        retrofit.create(GraduaApiService::class.java)
    }
}