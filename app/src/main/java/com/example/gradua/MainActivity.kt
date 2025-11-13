package com.example.gradua

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import com.example.gradua.screens.HomeScreen1
import com.example.gradua.screens.LoginScreen
import com.example.gradua.screens.RegisterScreen
import com.example.gradua.ui.GraduaBottomBar
import com.example.gradua.ui.theme.GraduaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GraduaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp()
                }
            }
        }
    }
}


@Composable
fun MainApp() {
    var currentScreen by remember { mutableStateOf("login") }
    var selectedBottomItem by remember { mutableStateOf(0) }
    // Define em quais telas a barra inferior deve aparecer
    val showBottomBar = currentScreen in listOf("home", "favoritos", "filtrar", "perfil")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                GraduaBottomBar(
                    selectedItem = selectedBottomItem,
                    onNavigate = { index ->
                        selectedBottomItem = index
                        currentScreen = when (index) {
                            0 -> "home"
                            1 -> "favoritos"
                            2 -> "filtrar"
                            3 -> "perfil"
                            else -> "home"
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                "login" -> {
                    LoginScreen(
                        onLoginSuccess = {
                            currentScreen = "home"
                            selectedBottomItem = 0
                        },
                        onRegisterClick = { currentScreen = "register" }
                    )
                }
                "register" -> {
                    RegisterScreen(
                        onBackClick = { currentScreen = "login" },
                        onRegisterSuccess = {
                            currentScreen = "home"
                            selectedBottomItem = 0
                        }
                    )
                }
                "home" -> { HomeScreen1() }
                "perfil" -> { PlaceholderScreen("Usuário") }
                "favoritos" -> { PlaceholderScreen("Favoritos") }
                "filtrar" -> { PlaceholderScreen("Filtrar") }
            }
        }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.Center) {
            Text("Tela de $name")
        }
    }
}
