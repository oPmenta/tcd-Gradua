package com.example.gradua.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gradua.ui.QuestionItem
import com.example.gradua.ui.theme.PurplePrimary
import com.example.gradua.viewModel.QuizUiState
import com.example.gradua.viewModel.QuizViewModel

@Composable
fun QuizScreen(
    materia: String?,
    conteudo: String,
    busca: String,
    isSimulado: Boolean,
    onBackClick: () -> Unit,
    viewModel: QuizViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(materia, conteudo, busca, isSimulado) {
        viewModel.fetchQuestions(context, materia, busca, conteudo, isSimulado)
    }

    val uiState = viewModel.uiState
    val apiQuestions = viewModel.questionsList

    val favoriteIds = viewModel.favoriteQuestionIds

    // Mapa de respostas: ID da Questão -> Letra da Resposta
    var selectedAnswers by remember { mutableStateOf(mapOf<Int, String>()) }

    var showFeedback by remember { mutableStateOf(false) }
    var showResultDialog by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var showOnlyErrors by remember { mutableStateOf(false) }

    val questionsDisplay = remember(apiQuestions, showOnlyErrors, showFeedback) {
        if (showOnlyErrors && showFeedback) {
            apiQuestions.filter { selectedAnswers[it.id] != it.gabarito }
        } else {
            apiQuestions
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),

        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PurplePrimary)
                        .windowInsetsTopHeight(WindowInsets.statusBars)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(PurplePrimary)
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = Color.White)
                    }
                    Text(
                        text = if (showFeedback) "Correção" else (materia ?: if(isSimulado) "Simulado Semanal" else "Todas as Questões"),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        },
        bottomBar = {
            if (uiState is QuizUiState.Success && apiQuestions.isNotEmpty()) {
                Surface(shadowElevation = 16.dp, color = Color.White) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {
                                if (showFeedback) onBackClick() else {

                                    var acertos = 0
                                    var erros = 0

                                    apiQuestions.forEach { q ->
                                        val respostaUsuario = selectedAnswers[q.id]

                                        if (respostaUsuario != null) {
                                            if (respostaUsuario == q.gabarito) {
                                                acertos++
                                            } else {
                                                erros++
                                            }
                                        }
                                    }

                                    score = acertos

                                    if (isSimulado) {
                                        viewModel.submitSimulado(context, acertos, erros)
                                    }

                                    showFeedback = true
                                    showResultDialog = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .height(50.dp),
                            shape = MaterialTheme.shapes.medium,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (showFeedback) Color.Red else PurplePrimary
                            )
                        ) {
                            Text(
                                text = if (showFeedback) "Sair" else "Finalizar Simulado",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
        ) {
            when (uiState) {
                is QuizUiState.Loading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = PurplePrimary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Carregando questões...", color = Color.Gray)
                    }
                }

                is QuizUiState.Error -> {
                    Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(uiState.message, color = Color.Red)
                        Button(onClick = { viewModel.fetchQuestions(context, materia, busca, conteudo, isSimulado) }) {
                            Text("Tentar Novamente")
                        }
                    }
                }

                is QuizUiState.Success -> {
                    if (apiQuestions.isEmpty()) {
                        Text("Nenhuma questão encontrada.", modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(questionsDisplay) { question ->
                                QuestionItem(
                                    question = question,
                                    selectedOption = selectedAnswers[question.id],
                                    showFeedback = showFeedback,
                                    isFavorite = favoriteIds.contains(question.remoteId),
                                    onFavoriteToggle = {
                                        viewModel.toggleFavorite(context, question.remoteId)
                                    },

                                    // --- LÓGICA DE SELEÇÃO E DESELEÇÃO ---
                                    onOptionSelected = { letra ->
                                        if (!showFeedback) {
                                            selectedAnswers = selectedAnswers.toMutableMap().apply {
                                                // Se clicar no que já está selecionado -> Remove (Deseleciona)
                                                if (this[question.id] == letra) {
                                                    remove(question.id)
                                                } else {
                                                    // Senão -> Seleciona o novo
                                                    put(question.id, letra)
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showResultDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Resultado", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("Você acertou $score de ${apiQuestions.size} questões!")

                        if (score == apiQuestions.size && apiQuestions.isNotEmpty()) {
                            Text("Parabéns! Gabaritou! 🏆", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        showResultDialog = false
                        showOnlyErrors = false
                    }) {
                        Text("Ver Correção")
                    }
                },
                dismissButton = { TextButton(onClick = { showResultDialog = false; onBackClick() }) { Text("Sair") } }
            )
        }
    }
}