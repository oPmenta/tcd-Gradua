package com.example.gradua.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gradua.data.UserStore
import com.example.gradua.ui.theme.PurplePrimary

@Composable
fun HomeScreen1(
    onDailyQuizClick: () -> Unit,
    onRankingClick: () -> Unit,
    onHistoryClick: () -> Unit // <--- NOVO PARÂMETRO ADICIONADO
) {
    val context = LocalContext.current
    val userStore = remember { UserStore(context) }
    val userName = remember { userStore.getUserName() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // CABEÇALHO
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Olá, $userName", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = "Vamos estudar hoje?", fontSize = 16.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // CARD RANKING
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onRankingClick() },
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier.padding(20.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Troféu",
                    modifier = Modifier.size(48.dp),
                    tint = Color(0xFFFFD700)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Ranking Semanal", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PurplePrimary)
                    Text("Veja sua posição atual!", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // BOTÃO SIMULADO
        Button(
            onClick = onDailyQuizClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(4.dp)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Iniciar Simulado Semanal", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // MENU INFERIOR
        Text("Outras opções", modifier = Modifier.align(Alignment.Start), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))

        // --- HISTÓRICO CONECTADO ---
        MenuItem(
            icon = Icons.Filled.Assignment,
            text = "Histórico de Simulados",
            iconTint = Color(0xFF2196F3),
            onClick = { onHistoryClick() } // <--- AGORA CHAMA A FUNÇÃO
        )
    }
}

@Composable
fun MenuItem(icon: ImageVector, text: String, iconTint: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
        }
    }
}