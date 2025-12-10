package com.example.gradua.network

import com.example.gradua.data.QuestionJson
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

private const val BASE_URL = "https://gradua.pythonanywhere.com/"
private const val TOKEN = "d9e8f7a6b5c4d3e2f1a0b9c8d7e6f5a4b3c2d1e0f9a8b7c6d5e4f3a2b1c0d9e8"

// --- CADASTRO ---
data class UserDto(
    @SerializedName("nome_completo") val nome: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("senha") val senha: String,
    @SerializedName("escola") val escola: String
)

data class RegisterResponse(
    @SerializedName("mensagem") val mensagem: String?,
    @SerializedName("erro") val erro: String?,
    @SerializedName("user_id") val userId: Int?
)

// --- LOGIN ---
data class LoginDto(
    @SerializedName("email") val email: String,
    @SerializedName("senha") val senha: String
)

// Ajustado para bater com o seu Python
data class LoginResponse(
    @SerializedName("mensagem") val mensagem: String?, // Python manda "Login OK"
    @SerializedName("user_id") val userId: Int?,
    @SerializedName("username") val username: String?,
    @SerializedName("nome") val nomeCompleto: String?, // Python manda "nome"
    // O Python NÃO manda escola no login, então isso virá nulo
    @SerializedName("escola") val escola: String?,
    @SerializedName("erro") val erro: String?
)

interface GraduaApiService {
    @GET("api/questoes")
    suspend fun getAllQuestions(
        @Header("Authorization") token: String = "Bearer $TOKEN",
        @Query("materia") materia: String? = null
    ): List<QuestionJson>

    @POST("api/auth/register")
    suspend fun registerUser(@Body userData: UserDto): RegisterResponse

    @POST("api/auth/login")
    suspend fun loginUser(@Body loginData: LoginDto): LoginResponse
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