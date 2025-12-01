package com.example.gradua.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gradua.ui.theme.PurplePrimary
import com.example.gradua.ui.theme.TextGray

// Modelo de dados simples para visualização
data class Question(
    val id: Int,
    val title: String,
    val subject: String,
    val year: String,
    val text: String
)

@Composable
fun FavoritesScreen() {
    // Lista fictícia de dados
    val favoriteQuestions = listOf(
        Question(1, "Questão 135", "Matemática", "2023", "Enunciado da questão sobre geometria..."),
        Question(2, "Questão 42", "Português", "2022", "Enunciado da questão sobre interpretação..."),
        Question(3, "Questão 90", "História", "2021", "Enunciado da questão sobre revolução...")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Questões Favoritas",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(favoriteQuestions) { question ->
                FavoriteItemCard(question)
            }
        }
    }
}

@Composable
fun FavoriteItemCard(question: Question) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)), // InputBg simulado
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = question.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "Matéria: ${question.subject}", fontSize = 14.sp, color = Color.Black)
                Text(text = "Ano da prova: ${question.year}", fontSize = 14.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = question.text,
                    fontSize = 12.sp,
                    color = TextGray,
                    maxLines = 1
                )
            }
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Favorito",
                tint = Color(0xFFFFC107), // Cor amarela/dourada para a estrela
                modifier = Modifier.size(32.dp)
            )
        }
    }
}