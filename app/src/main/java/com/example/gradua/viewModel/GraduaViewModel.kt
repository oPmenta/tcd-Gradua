package com.example.gradua.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gradua.data.AppDatabase
import com.example.gradua.data.Question
import com.example.gradua.data.UserProfile
import com.example.gradua.data.toFavorite
import com.example.gradua.data.toQuestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GraduaViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).favoriteDao()

    val favorites: StateFlow<List<Question>> = dao.getAllFavorites()
        .map { list -> list.map { it.toQuestion() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _userProfile = MutableStateFlow(UserProfile("Estudante Padrão", "aluno@email.com", "(11) 99999-9999"))
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    fun toggleFavorite(question: Question) {
        viewModelScope.launch {
            val isFavorite = favorites.value.any { it.id == question.id }
            if (isFavorite) {
                removeFavorite(question.id)
            } else {
                addFavorite(question)
            }
        }
    }

    fun addFavorite(question: Question) {
        viewModelScope.launch {
            dao.insertFavorite(question.toFavorite())
        }
    }

    fun removeFavorite(originalId: Int) {
        viewModelScope.launch {
            dao.deleteFavoriteByOriginalId(originalId)
        }
    }

    fun updateUser(newUser: UserProfile) {
        _userProfile.value = newUser
    }
}