package com.example.gradua.data

import android.content.Context
import android.content.SharedPreferences

class UserStore(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    // Salva os dados vindos da API na memória do celular
    fun registerUser(name: String, username: String, email: String, password: String, escola: String) {
        prefs.edit().apply {
            putString("USER_NAME", name)
            putString("USER_USERNAME", username)
            putString("USER_EMAIL", email)
            putString("USER_PASSWORD", password) // Opcional, mantido por compatibilidade
            putString("USER_ESCOLA", escola)
            putBoolean("IS_LOGGED", true)
            apply()
        }
    }

    // Marca que o usuário está logado
    // (Adicionei o parâmetro 'email' para compatibilidade com seu LoginScreen,
    // embora ele já tenha sido salvo no registerUser acima)
    fun login(email: String) {
        prefs.edit().apply {
            putBoolean("IS_LOGGED", true)
            putString("USER_EMAIL", email)
            apply()
        }
    }

    // Sobrecarga para caso chame sem argumentos
    fun login() {
        prefs.edit().putBoolean("IS_LOGGED", true).apply()
    }

    // Verifica se já tem alguém logado ao abrir o app
    fun isUserLoggedIn(): Boolean {
        return prefs.getBoolean("IS_LOGGED", false)
    }

    // Getters para mostrar os dados na tela de Perfil/Home
    fun getUserName(): String {
        return prefs.getString("USER_NAME", "Estudante") ?: "Estudante"
    }

    fun getUsername(): String {
        return prefs.getString("USER_USERNAME", "") ?: ""
    }

    fun getEscola(): String {
        return prefs.getString("USER_ESCOLA", "") ?: ""
    }

    fun logout() {
        prefs.edit().putBoolean("IS_LOGGED", false).apply()
    }

    // --- Parte de Favoritos (Mantida igual) ---
    fun getFavorites(): Set<String> {
        return prefs.getStringSet("FAVORITES_LIST", emptySet()) ?: emptySet()
    }

    fun toggleFavorite(questionId: String) {
        val currentFavorites = getFavorites().toMutableSet()
        if (currentFavorites.contains(questionId)) {
            currentFavorites.remove(questionId)
        } else {
            currentFavorites.add(questionId)
        }
        prefs.edit().putStringSet("FAVORITES_LIST", currentFavorites).apply()
    }

    fun isFavorite(questionId: String): Boolean {
        return getFavorites().contains(questionId)
    }
}