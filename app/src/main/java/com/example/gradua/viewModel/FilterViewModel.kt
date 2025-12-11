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

class FilterViewModel : ViewModel() {

    var isLoading by mutableStateOf(false)

    // Listas para os Dropdowns
    var availableSubjects by mutableStateOf<List<String>>(emptyList())
    var availableContents by mutableStateOf<List<String>>(emptyList())

    // Cache local de todas as questões
    private var allQuestionsCache: List<Question> = emptyList()

    init {
        loadFilters()
    }

    private fun loadFilters() {
        viewModelScope.launch {
            isLoading = true
            try {
                // Baixa tudo
                val result = GraduaServerApi.service.getAllQuestions(materia = null)
                allQuestionsCache = result.map { it.toQuestion() }

                // Extrai matérias únicas
                val subjects = allQuestionsCache
                    .map { it.materia }
                    .distinct()
                    .sorted()

                availableSubjects = listOf("Todas") + subjects

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun onSubjectSelected(subject: String) {
        // Filtra as questões da matéria selecionada (ou todas)
        val questionsToFilter = if (subject == "Todas") {
            allQuestionsCache
        } else {
            allQuestionsCache.filter { it.materia == subject }
        }

        // --- LÓGICA DE SEPARAÇÃO (SPLIT) ADICIONADA AQUI ---
        val contents = questionsToFilter
            .mapNotNull { it.conteudo } // Pega só quem tem conteúdo
            .flatMap { conteudoTexto ->
                // Quebra o texto onde tiver barra (/) ou vírgula (,)
                // Ex: "Cultura / Patrimônio" vira ["Cultura ", " Patrimônio"]
                conteudoTexto.split("/", ",")
            }
            .map { it.trim() } // Remove espaços extras (" Patrimônio" vira "Patrimônio")
            .filter { it.isNotEmpty() } // Remove vazios
            .distinct() // Remove duplicados
            .sorted() // Ordena alfabeticamente

        availableContents = listOf("Todos") + contents
    }
}