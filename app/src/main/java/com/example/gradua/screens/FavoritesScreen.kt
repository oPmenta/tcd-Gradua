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
import com.example.gradua.ui.QuestionItem
import com.example.gradua.ui.theme.PurplePrimary
import com.example.gradua.viewModel.FavoritesViewModel
import com.example.gradua.viewModel.QuizUiState

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = viewModel()
) {
    val context = LocalContext.current

    // Chama a API assim que a tela abre
    LaunchedEffect(Unit) {
        viewModel.loadFavorites(context)
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

        // Ajuste para não cortar o último item na barra de baixo
        val systemNavBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        val bottomPaddingTotal = 16.dp + systemNavBarHeight + 80.dp

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
        ) {
            when (val state = viewModel.uiState) {
                is QuizUiState.Loading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = PurplePrimary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Carregando...",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
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
                            contentPadding = PaddingValues(
                                top = 16.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = bottomPaddingTotal
                            ),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(viewModel.favoriteQuestions) { question ->
                                QuestionItem(
                                    question = question,
                                    selectedOption = null,
                                    showFeedback = false,
                                    isFavorite = true, // Na tela de favoritos, sempre é true
                                    onFavoriteToggle = {
                                        // Ao clicar no coração aqui, remove da lista e do banco
                                        viewModel.removeFavorite(context, question.remoteId)
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