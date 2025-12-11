package com.example.gradua.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gradua.data.Question
import com.example.gradua.data.UserStore
import com.example.gradua.data.toQuestion
import com.example.gradua.network.GraduaServerApi
import com.example.gradua.network.ToggleFavoriteRequest
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {

    var uiState: QuizUiState by mutableStateOf(QuizUiState.Loading)
        private set

    var favoriteQuestions: List<Question> by mutableStateOf(emptyList())
        private set

    // Carrega favoritos direto da API do Python
    fun loadFavorites(context: Context) {
        viewModelScope.launch {
            uiState = QuizUiState.Loading
            try {
                val userStore = UserStore(context)
                val userId = userStore.getUserId()

                if (userId != null) {
                    // Chama a API que busca no banco de dados
                    val resultJson = GraduaServerApi.service.getFavorites(userId = userId)
                    favoriteQuestions = resultJson.map { it.toQuestion() }
                    uiState = QuizUiState.Success
                } else {
                    uiState = QuizUiState.Error("Usuário não identificado.")
                }
            } catch (e: Exception) {
                uiState = QuizUiState.Error("Erro ao carregar favoritos: ${e.message}")
            }
        }
    }

    // Remove um favorito da lista (e do banco)
    fun removeFavorite(context: Context, questionId: String) {
        viewModelScope.launch {
            try {
                val userId = UserStore(context).getUserId() ?: return@launch

                // Avisa a API para remover
                GraduaServerApi.service.toggleFavorite(
                    userId = userId,
                    body = ToggleFavoriteRequest(questionId)
                )

                // Remove da lista local visualmente para não precisar recarregar tudo
                favoriteQuestions = favoriteQuestions.filter { it.remoteId != questionId }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}