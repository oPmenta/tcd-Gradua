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

    // Cache local de todas as questões para não baixar de novo ao trocar matéria
    private var allQuestionsCache: List<Question> = emptyList()

    init {
        loadFilters()
    }

    private fun loadFilters() {
        viewModelScope.launch {
            isLoading = true
            try {
                // Baixa tudo para extrair as matérias disponíveis
                val result = GraduaServerApi.service.getAllQuestions(materia = null)
                allQuestionsCache = result.map { it.toQuestion() }

                // Extrai matérias únicas e ordena
                val subjects = allQuestionsCache
                    .map { it.materia }
                    .distinct()
                    .sorted()

                // --- ADICIONA "Todas" NO INÍCIO ---
                availableSubjects = listOf("Todas") + subjects

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    // Chamado quando o usuário escolhe uma matéria no primeiro Dropdown
    fun onSubjectSelected(subject: String) {
        if (subject == "Todas") {
            // Se escolheu "Todas", o conteúdo pode ser qualquer um de qualquer matéria
            // (Ou você pode deixar apenas ["Todos"] se preferir simplificar)
            val allContents = allQuestionsCache
                .mapNotNull { it.conteudo } // Pega conteúdos não nulos
                .distinct()
                .sorted()

            availableContents = listOf("Todos") + allContents
        } else {
            // Filtra os conteúdos apenas daquela matéria
            val subjectContents = allQuestionsCache
                .filter { it.materia == subject }
                .mapNotNull { it.conteudo }
                .distinct()
                .sorted()

            // --- ADICIONA "Todos" NO INÍCIO ---
            availableContents = listOf("Todos") + subjectContents
        }
    }
}