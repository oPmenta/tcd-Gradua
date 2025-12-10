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

class FilterViewModel : ViewModel() {

    private var allQuestionsCache: List<Question> = emptyList()

    var isLoading by mutableStateOf(true)
    var availableSubjects by mutableStateOf<List<String>>(emptyList())
    var availableContents by mutableStateOf<List<String>>(emptyList())

    init {
        loadFilterData()
    }

    private fun loadFilterData() {
        viewModelScope.launch {
            try {
                isLoading = true
                val resultJson = GraduaServerApi.service.getAllQuestions(materia = null)
                allQuestionsCache = resultJson.map { it.toQuestion() }

                availableSubjects = allQuestionsCache
                    .map { it.materia }
                    .distinct()
                    .sorted()

                isLoading = false
            } catch (e: Exception) {
                Log.e("FilterViewModel", "Erro ao carregar filtros", e)
                isLoading = false
            }
        }
    }

    fun onSubjectSelected(subject: String) {
        // --- LÓGICA DE SEPARAÇÃO APLICADA AQUI ---
        availableContents = allQuestionsCache
            .filter { it.materia == subject }
            .mapNotNull { it.conteudo }
            // 1. Quebra a string onde tem "/"
            .flatMap { it.split("/") }
            // 2. Remove espaços em branco do começo e fim (ex: " Urbanização " vira "Urbanização")
            .map { it.trim() }
            // 3. Remove duplicatas e ordena
            .distinct()
            .sorted()
    }
}