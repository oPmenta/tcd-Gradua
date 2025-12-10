package com.example.gradua.data

import android.content.Context
import android.content.SharedPreferences

class UserStore(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    // Agora salvamos também a SENHA para poder validar depois
    fun registerUser(name: String, email: String, password: String) {
        prefs.edit().apply {
            putString("USER_NAME", name)
            putString("USER_EMAIL", email)
            putString("USER_PASSWORD", password) // Salva a senha
            putBoolean("IS_LOGGED", true)
            apply()
        }
    }

    // Função nova: Verifica se email e senha batem com o cadastro
    fun validateLogin(inputEmail: String, inputPassword: String): Boolean {
        val savedEmail = prefs.getString("USER_EMAIL", "")
        val savedPassword = prefs.getString("USER_PASSWORD", "")

        // Verifica se não está vazio e se é igual ao salvo
        return savedEmail == inputEmail && savedPassword == inputPassword
    }

    fun isUserLoggedIn(): Boolean {
        return prefs.getBoolean("IS_LOGGED", false)
    }

    fun getUserName(): String {
        return prefs.getString("USER_NAME", "Estudante") ?: "Estudante"
    }

    fun logout() {
        // Ao deslogar, mudamos o flag, mas mantemos o cadastro para a pessoa logar de novo
        prefs.edit().putBoolean("IS_LOGGED", false).apply()
    }

    fun getFavorites(): Set<String> {
        return prefs.getStringSet("FAVORITES_LIST", emptySet()) ?: emptySet()
    }

    // Adiciona ou Remove dos favoritos
    fun toggleFavorite(questionId: String) {
        val currentFavorites = getFavorites().toMutableSet()

        if (currentFavorites.contains(questionId)) {
            currentFavorites.remove(questionId) // Remove se já tiver
        } else {
            currentFavorites.add(questionId) // Adiciona se não tiver
        }

        prefs.edit().putStringSet("FAVORITES_LIST", currentFavorites).apply()
    }

    // Verifica se um específico é favorito (útil para pintar o coração)
    fun isFavorite(questionId: String): Boolean {
        return getFavorites().contains(questionId)
    }
}