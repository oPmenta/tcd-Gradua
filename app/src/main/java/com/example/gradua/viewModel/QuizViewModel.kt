package com.example.gradua.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gradua.data.Question
import com.example.gradua.data.UserStore
import com.example.gradua.data.toQuestion
import com.example.gradua.network.GraduaServerApi
import com.example.gradua.network.SimuladoFinalRequest
import com.example.gradua.network.ToggleFavoriteRequest
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

sealed interface QuizUiState {
    object Success : QuizUiState
    object Loading : QuizUiState
    data class Error(val message: String) : QuizUiState
}

class QuizViewModel : ViewModel() {

    var uiState: QuizUiState by mutableStateOf(QuizUiState.Loading)
        private set

    var questionsList: List<Question> by mutableStateOf(emptyList())
        private set

    var favoriteQuestionIds = mutableStateListOf<String>()
        private set

    fun fetchQuestions(context: Context, filtroMateria: String?, termoBusca: String, filtroConteudo: String, isSimulado: Boolean) {
        viewModelScope.launch {
            uiState = QuizUiState.Loading
            try {
                val userStore = UserStore(context)
                val userId = userStore.getUserId()

                val questionsDeferred = async {
                    val materiaParaApi = if (filtroMateria == "Todas" || filtroMateria.isNullOrEmpty()) null else filtroMateria
                    val json = GraduaServerApi.service.getAllQuestions(materia = materiaParaApi)
                    json.map { it.toQuestion() }
                }

                val favoritesDeferred = async {
                    if (userId != null) {
                        try {
                            val favs = GraduaServerApi.service.getFavorites(userId)
                            favs.map { it.id }
                        } catch (e: Exception) {
                            emptyList<String>()
                        }
                    } else {
                        emptyList<String>()
                    }
                }

                var allQuestions = questionsDeferred.await()
                val userFavorites = favoritesDeferred.await()

                favoriteQuestionIds.clear()
                favoriteQuestionIds.addAll(userFavorites)

                // Filtros Locais
                if (termoBusca.isNotEmpty()) {
                    allQuestions = allQuestions.filter {
                        it.enunciado.contains(termoBusca, ignoreCase = true)
                    }
                }

                if (filtroConteudo.isNotEmpty() && filtroConteudo != "Todos") {
                    allQuestions = allQuestions.filter { questao ->
                        questao.conteudo?.contains(filtroConteudo, ignoreCase = true) == true
                    }
                }

                // --- LÓGICA DE SIMULADO ALTERADA ---
                if (isSimulado) {
                    val simuladoList = allQuestions
                        .groupBy { it.materia }
                        .flatMap { (_, lista) ->
                            // MUDANÇA AQUI: take(1) em vez de take(3)
                            lista.shuffled().take(1)
                        }
                        .shuffled()
                    questionsList = simuladoList
                } else {
                    questionsList = allQuestions
                }

                uiState = QuizUiState.Success

            } catch (e: Exception) {
                Log.e("QuizViewModel", "Erro", e)
                uiState = QuizUiState.Error("Erro ao carregar: ${e.message}")
            }
        }
    }

    fun toggleFavorite(context: Context, questionId: String) {
        viewModelScope.launch {
            try {
                val userId = UserStore(context).getUserId() ?: return@launch
                val response = GraduaServerApi.service.toggleFavorite(userId = userId, body = ToggleFavoriteRequest(questionId))
                favoriteQuestionIds.clear()
                favoriteQuestionIds.addAll(response.favoritos)
            } catch (e: Exception) {
                Log.e("QuizViewModel", "Erro ao favoritar", e)
            }
        }
    }

    fun submitSimulado(context: Context, acertos: Int, erros: Int) {
        viewModelScope.launch {
            try {
                val userId = UserStore(context).getUserId() ?: return@launch
                GraduaServerApi.service.finalizarSimulado(userId = userId, body = SimuladoFinalRequest(acertos, erros))
                Log.d("QuizViewModel", "Simulado enviado com sucesso")
            } catch (e: Exception) {
                Log.e("QuizViewModel", "Erro ao enviar simulado", e)
            }
        }
    }
}