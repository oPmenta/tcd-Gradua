package com.example.gradua.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gradua.data.Question
import com.example.gradua.data.toQuestion
import com.example.gradua.network.GraduaServerApi
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {

    var uiState: QuizUiState by mutableStateOf(QuizUiState.Loading)
        private set

    var favoriteQuestions: List<Question> by mutableStateOf(emptyList())
        private set

    // Função para carregar apenas os favoritos
    fun loadFavorites(savedIds: Set<String>) {
        if (savedIds.isEmpty()) {
            uiState = QuizUiState.Success
            favoriteQuestions = emptyList()
            return
        }

        viewModelScope.launch {
            uiState = QuizUiState.Loading
            try {
                // 1. Baixa todas as questões (cacheie se possível no futuro)
                val resultJson = GraduaServerApi.service.getAllQuestions(materia = null)
                val allQuestions = resultJson.map { it.toQuestion() }

                // 2. Filtra apenas as que estão na lista de IDs salvos
                favoriteQuestions = allQuestions.filter { question ->
                    savedIds.contains(question.remoteId)
                }

                uiState = QuizUiState.Success
            } catch (e: Exception) {
                uiState = QuizUiState.Error("Erro ao carregar favoritos: ${e.message}")
            }
        }
    }
}