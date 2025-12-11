package com.example.gradua

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import com.example.gradua.screens.LoginScreen
import com.example.gradua.screens.HomeScreen1
import com.example.gradua.screens.QuizScreen
import com.example.gradua.screens.FavoritesScreen
import com.example.gradua.screens.FilterScreen
import com.example.gradua.screens.ProfileScreen
import com.example.gradua.screens.RegisterScreen
import com.example.gradua.screens.RankingScreen
import com.example.gradua.screens.HistoryScreen

import com.example.gradua.ui.GraduaBottomBar
import com.example.gradua.data.UserStore
import com.example.gradua.ui.theme.GraduaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GraduaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val userStore = remember { UserStore(context) }

    var currentScreen by remember {
        mutableStateOf(if (userStore.isUserLoggedIn()) "home" else "login")
    }

    var filtroMateriaAtual by remember { mutableStateOf<String?>(null) }
    var filtroConteudoAtual by remember { mutableStateOf("") }
    var filtroBuscaAtual by remember { mutableStateOf("") }

    var isSimuladoMode by remember { mutableStateOf(true) }

    var selectedBottomItem by remember { mutableIntStateOf(0) }

    // Oculta a barra de baixo nessas telas
    val showBottomBar = currentScreen !in listOf("login", "cadastro", "simulado_semanal")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                GraduaBottomBar(
                    selectedItem = selectedBottomItem,
                    onNavigate = { index: Int ->
                        selectedBottomItem = index
                        currentScreen = when (index) {
                            0 -> "home"
                            1 -> "favoritos"
                            2 -> "filtrar"
                            3 -> "perfil" // <--- Isso define currentScreen = "perfil"
                            else -> "home"
                        }
                    }
                )
            }
        }
    ) { paddingValues ->

        // --- LÓGICA DE TELA CHEIA (Edge-to-Edge) ---
        // Adicionei "perfil" aqui para a barra roxa subir até o topo
        val modifier = if (
            currentScreen == "simulado_semanal" ||
            currentScreen == "favoritos" ||
            currentScreen == "ranking" ||
            currentScreen == "historico" ||
            currentScreen == "login" ||
            currentScreen == "cadastro" ||
            currentScreen == "perfil" // <--- ADICIONADO AGORA
        ) {
            Modifier.fillMaxSize()
        } else {
            Modifier.fillMaxSize().padding(paddingValues)
        }

        Box(modifier = modifier) {
            when (currentScreen) {
                "login" -> {
                    LoginScreen(
                        onLoginSuccess = {
                            currentScreen = "home"
                            selectedBottomItem = 0
                        },
                        onRegisterClick = { currentScreen = "cadastro" }
                    )
                }
                "cadastro" -> {
                    RegisterScreen(
                        onBackClick = { currentScreen = "login" },
                        onRegisterSuccess = {
                            currentScreen = "home"
                            selectedBottomItem = 0
                        }
                    )
                }
                "home" -> {
                    HomeScreen1(
                        onDailyQuizClick = {
                            filtroMateriaAtual = null
                            filtroConteudoAtual = ""
                            filtroBuscaAtual = ""
                            isSimuladoMode = true
                            currentScreen = "simulado_semanal"
                        },
                        onRankingClick = {
                            currentScreen = "ranking"
                        },
                        onHistoryClick = {
                            currentScreen = "historico"
                        }
                    )
                }
                "ranking" -> {
                    RankingScreen(
                        onBackClick = { currentScreen = "home" }
                    )
                }
                "historico" -> {
                    HistoryScreen(
                        onBackClick = { currentScreen = "home" }
                    )
                }
                "simulado_semanal" -> {
                    QuizScreen(
                        materia = filtroMateriaAtual,
                        conteudo = filtroConteudoAtual,
                        busca = filtroBuscaAtual,
                        isSimulado = isSimuladoMode,
                        onBackClick = {
                            currentScreen = "home"
                            selectedBottomItem = 0
                        }
                    )
                }
                "favoritos" -> { FavoritesScreen() }
                "filtrar" -> {
                    FilterScreen(
                        onFilterApplied = { materia, busca, conteudo ->
                            filtroMateriaAtual = materia
                            filtroBuscaAtual = busca
                            filtroConteudoAtual = conteudo
                            isSimuladoMode = false
                            currentScreen = "simulado_semanal"
                        }
                    )
                }
                "perfil" -> {
                    ProfileScreen(
                        onLogout = {
                            userStore.logout()
                            currentScreen = "login"
                            selectedBottomItem = 0
                        }
                    )
                }
            }
        }
    }
}