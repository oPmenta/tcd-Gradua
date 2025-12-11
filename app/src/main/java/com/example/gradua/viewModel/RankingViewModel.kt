package com.example.gradua.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gradua.data.UserStore
import com.example.gradua.network.GraduaServerApi
import com.example.gradua.network.RankingItem
import kotlinx.coroutines.launch

class RankingViewModel : ViewModel() {

    var rankingList by mutableStateOf<List<RankingItem>>(emptyList())
    var myRanking by mutableStateOf<RankingItem?>(null)
    var isLoading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)

    fun fetchRanking(context: Context) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                // 1. Pega o ID do usuário salvo no login
                val userStore = UserStore(context)
                val userId = userStore.getUserId() ?: ""

                if (userId.isNotEmpty()) {
                    // 2. Chama a API enviando o ID no Header
                    val response = GraduaServerApi.service.getRanking(userId = userId)

                    rankingList = response.ranking
                    myRanking = response.meuRanking
                } else {
                    errorMessage = "Você precisa estar logado para ver o ranking."
                }
            } catch (e: Exception) {
                Log.e("RankingViewModel", "Erro", e)
                errorMessage = "Erro ao carregar ranking. Verifique sua conexão."
            } finally {
                isLoading = false
            }
        }
    }
}