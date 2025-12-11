package com.example.gradua.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.gradua.network.HistoricoItem
import com.example.gradua.ui.theme.PurplePrimary
import com.example.gradua.viewModel.HistoryViewModel

@Composable
fun HistoryScreen(
    onBackClick: () -> Unit,
    viewModel: HistoryViewModel = viewModel()
) {
    val context = LocalContext.current

    // Carrega ao abrir
    LaunchedEffect(Unit) {
        viewModel.fetchHistory(context)
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp), // Visual Edge-to-Edge

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
                            text = "Histórico de Simulados",
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
                    Text("Carregando histórico...", color = Color.Gray)
                }
            } else if (viewModel.errorMessage != null) {
                Text(
                    text = viewModel.errorMessage!!,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                if (viewModel.historyList.isEmpty()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Você ainda não realizou simulados.", color = Color.Gray)
                    }
                } else {

                    // --- CORREÇÃO DO SCROLL ---
                    // 1. Altura da barra do sistema (botões virtuais/gestos do Android)
                    val systemNavBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

                    // 2. Altura da NOSSA barra de menu (GraduaBottomBar) ~ 80dp
                    // Somamos tudo para o último card não ficar escondido
                    val bottomPaddingTotal = 16.dp + systemNavBarHeight + 80.dp

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = 16.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = bottomPaddingTotal // <--- APLICADO AQUI
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(viewModel.historyList) { item ->
                            HistoryCard(item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryCard(item: HistoricoItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Data e Pontos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.data,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 16.sp
                )

                Text(
                    text = "${item.pontos} pts",
                    fontWeight = FontWeight.Bold,
                    color = PurplePrimary,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color(0xFFEEEEEE))
            Spacer(modifier = Modifier.height(8.dp))

            // Detalhes de Acertos/Erros
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("✅ Acertos: ${item.acertos}", color = Color(0xFF4CAF50), fontWeight = FontWeight.Medium)
                Text("❌ Erros: ${item.erros}", color = Color(0xFFE53935), fontWeight = FontWeight.Medium)
            }
        }
    }
}