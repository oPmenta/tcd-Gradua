package com.example.gradua.network

import com.example.gradua.data.QuestionJson
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

// --- MODELOS DE DADOS ---

// Login/Registro
data class LoginRequest(val username: String, val senha: String)
data class RegisterRequest(val username: String, val senha: String, @SerializedName("nome_completo") val nomeCompleto: String, val escola: String)
data class AuthResponse(val mensagem: String?, val erro: String?, @SerializedName("user_id") val userId: Int?, val username: String?)

// Favoritos
data class ToggleFavoriteRequest(@SerializedName("questao_id") val questaoId: String)
data class ToggleFavoriteResponse(val mensagem: String, val favoritos: List<String>) // Retorna lista atualizada de IDs

// Histórico e Simulado
data class SimuladoFinalRequest(val acertos: Int, val erros: Int)
data class SimuladoFinalResponse(val mensagem: String, @SerializedName("ranking_total") val rankingTotal: Int)

data class HistoricoItem(
    val id: Int,
    val data: String,
    val acertos: Int,
    val erros: Int,
    val pontos: Int
)

// Ranking (Mantido)
data class RankingResponse(val ranking: List<RankingItem>, @SerializedName("meu_ranking") val meuRanking: RankingItem?)
data class RankingItem(val posicao: Any, val nome: String, val pontos: Int, @SerializedName("is_me") val isMe: Boolean)

data class ProfileResponse(
    val username: String,
    @SerializedName("nome_completo") val nomeCompleto: String?,
    val escola: String?,
    @SerializedName("pontuacao_semanal") val pontuacao: Int
)

// --- INTERFACE RETROFIT ---

private const val BASE_URL = "https://gradua.pythonanywhere.com/"
private const val ADMIN_TOKEN = "d9e8f7a6b5c4d3e2f1a0b9c8d7e6f5a4b3c2d1e0f9a8b7c6d5e4f3a2b1c0d9e8"

interface GraduaApiService {
    // Questões
    @GET("api/questoes")
    suspend fun getAllQuestions(@Header("Authorization") token: String = "Bearer $ADMIN_TOKEN", @Query("materia") materia: String? = null): List<QuestionJson>

    // Auth
    @POST("api/auth/register")
    suspend fun registerUser(@Body request: RegisterRequest): AuthResponse
    @POST("api/auth/login")
    suspend fun loginUser(@Body request: LoginRequest): AuthResponse

    // Favoritos
    @POST("api/favoritos/toggle")
    suspend fun toggleFavorite(@Header("X-User-ID") userId: String, @Body body: ToggleFavoriteRequest): ToggleFavoriteResponse

    @GET("api/favoritos")
    suspend fun getFavorites(@Header("X-User-ID") userId: String): List<QuestionJson>

    // Ranking e Histórico
    @GET("api/ranking")
    suspend fun getRanking(@Header("X-User-ID") userId: String): RankingResponse

    @POST("api/simulado/finalizar")
    suspend fun finalizarSimulado(@Header("X-User-ID") userId: String, @Body body: SimuladoFinalRequest): SimuladoFinalResponse

    @GET("api/historico")
    suspend fun getHistorico(@Header("X-User-ID") userId: String): List<HistoricoItem>

    @GET("api/perfil")

    suspend fun getProfile(@Header("X-User-ID") userId: String): ProfileResponse
}

// --- CLIENTE ---
object GraduaServerApi {
    private val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    private val client = OkHttpClient.Builder().addInterceptor(logging).build()
    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(client).build()
    val service: GraduaApiService by lazy { retrofit.create(GraduaApiService::class.java) }
}