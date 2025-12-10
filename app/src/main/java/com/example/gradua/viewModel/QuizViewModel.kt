package com.example.gradua.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gradua.data.Question
import com.example.gradua.data.toQuestion
import com.example.gradua.network.GraduaServerApi
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

    // Função atualizada com o parâmetro 'isSimulado'
    fun fetchQuestions(filtroMateria: String?, termoBusca: String, filtroConteudo: String, isSimulado: Boolean) {
        viewModelScope.launch {
            uiState = QuizUiState.Loading
            try {
                // 1. Filtro de Matéria (Via API)
                val materiaParaApi = if (filtroMateria == "Todas" || filtroMateria.isNullOrEmpty()) null else filtroMateria

                val resultJson = GraduaServerApi.service.getAllQuestions(materia = materiaParaApi)
                var allQuestions = resultJson.map { it.toQuestion() }

                // 2. Filtro de Busca (Enunciado)
                if (termoBusca.isNotEmpty()) {
                    allQuestions = allQuestions.filter {
                        it.enunciado.contains(termoBusca, ignoreCase = true)
                    }
                }

                // 3. Filtro de Conteúdo (Local)
                if (filtroConteudo.isNotEmpty()) {
                    allQuestions = allQuestions.filter { questao ->
                        questao.conteudo?.contains(filtroConteudo, ignoreCase = true) == true
                    }
                }

                // 4. DECISÃO DE LÓGICA (Corrigida)
                if (isSimulado) {
                    // MODO SIMULADO: Sorteia 3 questões de cada matéria
                    val simuladoList = allQuestions
                        .groupBy { it.materia }
                        .flatMap { (_, lista) -> lista.shuffled().take(3) }
                        .shuffled()
                    questionsList = simuladoList
                } else {
                    // MODO FILTRO/NAVEGAÇÃO: Mostra TUDO que encontrou
                    // (Se não filtrou nada, mostra todas as questões do banco)
                    questionsList = allQuestions
                }

                uiState = QuizUiState.Success

            } catch (e: Exception) {
                Log.e("QuizViewModel", "Erro", e)
                uiState = QuizUiState.Error("Erro ao carregar: ${e.message}")
            }
        }
    }
}