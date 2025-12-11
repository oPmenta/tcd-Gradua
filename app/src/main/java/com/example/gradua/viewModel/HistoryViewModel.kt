package com.example.gradua.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gradua.data.UserStore
import com.example.gradua.network.GraduaServerApi
import com.example.gradua.network.HistoricoItem
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    var historyList by mutableStateOf<List<HistoricoItem>>(emptyList())
    var isLoading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)

    fun fetchHistory(context: Context) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val userId = UserStore(context).getUserId()

                if (userId != null) {
                    // Chama a API
                    historyList = GraduaServerApi.service.getHistorico(userId)
                } else {
                    errorMessage = "Erro de identificação do usuário."
                }
            } catch (e: Exception) {
                errorMessage = "Não foi possível carregar o histórico."
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}