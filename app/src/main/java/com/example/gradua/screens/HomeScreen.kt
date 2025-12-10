package com.example.gradua.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.EmojiEvents
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
import com.example.gradua.data.UserStore // <--- Novo Import
import com.example.gradua.ui.theme.PurplePrimary

@Composable
fun HomeScreen1(onDailyQuizClick: () -> Unit) {
    val context = LocalContext.current
    val userStore = remember { UserStore(context) }

    // Pega o nome salvo no UserStore
    val userName = remember { userStore.getUserName() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cabeçalho
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Olá, $userName", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "Vamos estudar hoje?", fontSize = 16.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Card de Destaque
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Progresso Semanal", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1976D2))
                Spacer(modifier = Modifier.height(8.dp))
                Icon(imageVector = Icons.Filled.EmojiEvents, contentDescription = "Troféu", modifier = Modifier.size(80.dp), tint = Color(0xFFFFC107))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botão Simulado (Chama a API)
        Button(
            onClick = onDailyQuizClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Simulado Geral", fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Outros itens de menu (Decorativos por enquanto)
        MenuItem(icon = Icons.Filled.EmojiEvents, text = "Ranking", iconTint = Color(0xFFFF9800))
        MenuItem(icon = Icons.Filled.Assignment, text = "Histórico", iconTint = Color(0xFF2196F3))
    }
}

@Composable
fun MenuItem(icon: ImageVector, text: String, iconTint: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}