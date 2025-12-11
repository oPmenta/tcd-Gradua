package com.example.gradua.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gradua.data.UserStore
import com.example.gradua.network.GraduaServerApi
import com.example.gradua.network.ProfileResponse
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    var profileData by mutableStateOf<ProfileResponse?>(null)
    var isLoading by mutableStateOf(true)

    fun fetchProfile(context: Context) {
        viewModelScope.launch {
            isLoading = true
            try {
                val userId = UserStore(context).getUserId()
                if (userId != null) {
                    profileData = GraduaServerApi.service.getProfile(userId)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}