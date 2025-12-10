package com.example.gradua.data

import com.google.gson.annotations.SerializedName

// 1. JSON (Vem do Python) - Mantenha igual
data class QuestionJson(
    val id: String,
    val materia: String,
    val conteudo: String?, // O JSON tem isso
    val enunciado: String,
    val alternativas: List<AlternativeJson>,
    val gabarito: String,
    @SerializedName("imagem_url")
    val imagemUrl: String? = null
)

data class AlternativeJson(
    val letra: String,
    val texto: String
)

// 2. Modelo do App - ADICIONE O CAMPO 'conteudo'
data class Question(
    val id: Int,
    val remoteId: String,
    val materia: String,
    val conteudo: String?, // <--- NOVO CAMPO ADICIONADO AQUI
    val enunciado: String,
    val alternativas: List<AlternativeJson>,
    val gabarito: String,
    val imagemPath: String?
)

// 3. Conversor - MAPEIE O CAMPO NOVO
fun QuestionJson.toQuestion(): Question {

    val fullUrl = if (!this.imagemUrl.isNullOrEmpty()) {
        val nomeLimpo = this.imagemUrl.substringAfterLast("/").trim()
        "https://gradua.pythonanywhere.com/static/imagens_uuid/$nomeLimpo"
    } else {
        null
    }

    return Question(
        id = this.id.hashCode(),
        remoteId = this.id,
        materia = this.materia,
        conteudo = this.conteudo, // <--- MAPEANDO O CAMPO AQUI
        enunciado = this.enunciado,
        alternativas = this.alternativas,
        gabarito = this.gabarito,
        imagemPath = fullUrl
    )
}