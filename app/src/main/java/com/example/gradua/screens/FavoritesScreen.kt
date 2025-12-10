package com.example.gradua.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gradua.data.UserStore
import com.example.gradua.ui.QuestionItem
import com.example.gradua.ui.theme.PurplePrimary
import com.example.gradua.viewModel.FavoritesViewModel
import com.example.gradua.viewModel.QuizUiState

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = viewModel()
) {
    val context = LocalContext.current
    val userStore = remember { UserStore(context) }

    var favoriteIds by remember { mutableStateOf(userStore.getFavorites()) }

    LaunchedEffect(favoriteIds) {
        viewModel.loadFavorites(favoriteIds)
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),

        topBar = {
            Surface(
                color = PurplePrimary,
                shadowElevation = 4.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(PurplePrimary)
                            .windowInsetsTopHeight(WindowInsets.statusBars)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(PurplePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Meus Favoritos",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { paddingValues ->

        // --- CORREÇÃO FINAL DO CORTE ---
        // 1. Altura da barra de gestos do Android
        val systemNavBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

        // 2. Altura aproximada do Menu do App (BottomBar) + Margem de segurança
        // Geralmente a BottomBar tem 80dp. Somamos com a barra do sistema.
        val bottomPaddingTotal = 16.dp + systemNavBarHeight + 80.dp

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
        ) {
            when (val state = viewModel.uiState) {
                is QuizUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is QuizUiState.Error -> {
                    Text(
                        text = state.message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is QuizUiState.Success -> {
                    if (viewModel.favoriteQuestions.isEmpty()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Você ainda não tem favoritos.", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(
                            // AQUI APLICAMOS O PADDING CALCULADO
                            contentPadding = PaddingValues(
                                top = 16.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = bottomPaddingTotal // Garante que suba acima do menu
                            ),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(viewModel.favoriteQuestions) { question ->
                                QuestionItem(
                                    question = question,
                                    selectedOption = null,
                                    showFeedback = false,
                                    isFavorite = true,
                                    onFavoriteToggle = {
                                        userStore.toggleFavorite(question.remoteId)
                                        favoriteIds = userStore.getFavorites()
                                    },
                                    onOptionSelected = { }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}