package com.example.gradua.data

import android.content.Context
import android.content.SharedPreferences

class UserStore(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_ID = "USER_ID"
        private const val KEY_USER_NAME = "USER_NAME"
        private const val KEY_IS_LOGGED = "IS_LOGGED"
    }

    // --- SESSÃO (LOGIN/LOGOUT) ---

    // Atualizado: Removemos 'email', agora salvamos apenas ID e Username
    fun saveUserSession(userId: String, username: String) {
        prefs.edit().apply {
            putString(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, username)
            putBoolean(KEY_IS_LOGGED, true)
            apply()
        }
    }

    fun isUserLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED, false) && !getUserId().isNullOrEmpty()
    }

    fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }

    fun getUserName(): String {
        return prefs.getString(KEY_USER_NAME, "Estudante") ?: "Estudante"
    }

    fun logout() {
        // Limpa todos os dados locais ao sair
        prefs.edit().clear().apply()
    }

    // --- NOTA IMPORTANTE ---
    // As funções de Favoritos (getFavorites, toggleFavorite, etc.) foram REMOVIDAS daqui.
    // Agora o aplicativo deve buscar essas informações diretamente da API (GraduaServerApi)
    // através dos ViewModels (FavoritesViewModel e QuizViewModel).
}