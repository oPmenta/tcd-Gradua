package com.example.gradua.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    // Cria ou recupera um arquivo de preferências chamado "user_session"
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_EMAIL = "USER_EMAIL"
    }

    /**
     * Salva o email do usuário logado para manter a sessão ativa.
     */
    fun saveUserSession(email: String) {
        val editor = prefs.edit()
        editor.putString(KEY_EMAIL, email)
        editor.apply() // Salva de forma assíncrona
    }

    /**
     * Retorna o email do usuário logado ou null se não houver ninguém logado.
     */
    fun getUserEmail(): String? {
        return prefs.getString(KEY_EMAIL, null)
    }

    /**
     * Limpa os dados da sessão (usado no logout).
     */
    fun logout() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}