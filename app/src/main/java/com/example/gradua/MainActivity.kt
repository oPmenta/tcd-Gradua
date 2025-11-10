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
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gradua.screens.HomeScreen1
import com.example.gradua.screens.LoginScreen
import com.example.gradua.screens.RegisterScreen
import com.example.gradua.screens.UserProfileScreen
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
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Define em quais rotas a barra inferior deve aparecer
    val showBottomBar = currentRoute in listOf("home", "favoritos", "filtrar", "perfil")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                val selectedItem = when (currentRoute) {
                    "home" -> 0
                    "favoritos" -> 1
                    "filtrar" -> 2
                    "perfil" -> 3
                    else -> 0
                }

                GraduaBottomBar(
                    selectedItem = selectedItem,
                    onNavigate = { index ->
                        val route = when (index) {
                            0 -> "home"
                            1 -> "favoritos"
                            2 -> "filtrar"
                            3 -> "perfil"
                            else -> "home"
                        }
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onRegisterClick = { navController.navigate("register") }
                )
            }

            composable("register") {
                RegisterScreen(
                    onBackClick = { navController.popBackStack() },
                    onRegisterSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }

            composable("home") { HomeScreen1() }

            composable("perfil") { UserProfileScreen() }

            // Placeholders para as outras abas
            composable("favoritos") { PlaceholderScreen("Favoritos") }
            composable("filtrar") { PlaceholderScreen("Filtrar") }
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