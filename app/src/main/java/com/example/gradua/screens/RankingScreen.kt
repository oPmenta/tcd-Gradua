package com.example.gradua.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gradua.network.RankingItem
import com.example.gradua.ui.theme.PurplePrimary
import com.example.gradua.viewModel.RankingViewModel

@Composable
fun RankingScreen(
    onBackClick: () -> Unit,
    viewModel: RankingViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchRanking(context)
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),

        topBar = {
            Surface(color = PurplePrimary, shadowElevation = 4.dp) {
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
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = Color.White)
                        }
                        Text(
                            text = "Ranking Semanal",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
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
            if (viewModel.isLoading) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = PurplePrimary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Carregando ranking...", color = Color.Gray)
                }
            } else if (viewModel.errorMessage != null) {
                Text(
                    text = viewModel.errorMessage!!,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp)
                )
            } else {
                Column(modifier = Modifier.fillMaxSize()) {

                    // CARD DESTAQUE (SUA POSIÇÃO)
                    viewModel.myRanking?.let { me ->
                        Surface(
                            color = Color.White,
                            shadowElevation = 4.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Sua Posição Atual", color = Color.Gray, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                RankingCard(item = me, isHighlight = true)
                            }
                        }
                    }

                    // --- CORREÇÃO DO SCROLL E PADDING INFERIOR ---
                    // Como a tela de Ranking exibe a Barra de Menu embaixo,
                    // precisamos somar a altura dela (80dp) + a barra do sistema Android.
                    val systemNavBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                    val bottomPaddingTotal = 16.dp + systemNavBarHeight + 80.dp

                    LazyColumn(
                        contentPadding = PaddingValues(
                            top = 16.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = bottomPaddingTotal // <--- ESPAÇO EXTRA AQUI
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize() // Garante o scroll
                    ) {
                        items(viewModel.rankingList) { item ->
                            RankingCard(item = item, isHighlight = item.isMe)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RankingCard(item: RankingItem, isHighlight: Boolean) {
    val borderColor = if (isHighlight) PurplePrimary else Color.Transparent
    val backgroundColor = if (isHighlight) PurplePrimary.copy(alpha = 0.05f) else Color.White

    // Remove o ".0" caso venha do backend
    val posFormatada = item.posicao.toString().replace(".0", "")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Círculo da Posição
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            when (posFormatada) {
                                "1" -> Color(0xFFFFD700)
                                "2" -> Color(0xFFC0C0C0)
                                "3" -> Color(0xFFCD7F32)
                                else -> PurplePrimary
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${posFormatada}º",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Nome
                Column {
                    Text(
                        text = item.nome,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    if (isHighlight) {
                        Text("Você", color = PurplePrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Pontos
            Text(
                text = "${item.pontos} pts",
                fontWeight = FontWeight.Bold,
                color = PurplePrimary,
                fontSize = 16.sp
            )
        }
    }
}