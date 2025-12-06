package com.example.gradua.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gradua.data.DatabaseHelper
import com.example.gradua.ui.theme.PurplePrimary
import com.example.gradua.utils.SessionManager

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val dbHelper = remember { DatabaseHelper(context) }
    val sessionManager = remember { SessionManager(context) }

    // Estados para armazenar os dados do usuário
    var userName by remember { mutableStateOf("Usuário") }
    var userSchool by remember { mutableStateOf("Escola não informada") }
    var userExam by remember { mutableStateOf("-") }

    // Carregar dados ao iniciar a tela
    LaunchedEffect(Unit) {
        val email = sessionManager.getUserEmail()
        if (email != null) {
            val details = dbHelper.getUserDetails(email)
            if (details != null) {
                userName = details["name"] ?: "Usuário"
                userSchool = details["school"] ?: ""
                userExam = details["examType"] ?: "-"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Cabeçalho com Ícone e Nome
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 32.dp)
                .semantics(mergeDescendants = true) {
                    contentDescription = "Perfil de $userName, estudante de $userSchool"
                }
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFE0E0E0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null, // Descrição já está no Row pai
                    modifier = Modifier.size(50.dp),
                    tint = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = userName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.semantics { heading() }
                )
                if (userSchool.isNotEmpty()) {
                    Text(text = userSchool, fontSize = 14.sp, color = Color.Gray)
                }
                Text(text = "Foco: $userExam", fontSize = 14.sp, color = PurplePrimary)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Seção Meus dados
        Text(
            text = "Meus dados",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.semantics { heading() }
        )
        Spacer(modifier = Modifier.height(16.dp))

        ProfileMenuItem(icon = Icons.Filled.Info, text = "Informações da conta")
        // Mostra a escola como um item de informação também, se desejar
        ProfileMenuItem(icon = Icons.Filled.School, text = "Escola: $userSchool")

        Spacer(modifier = Modifier.height(32.dp))

        // Seção Configurações
        Text(
            text = "Configurações",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.semantics { heading() }
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProfileMenuItem(icon = Icons.Filled.Accessibility, text = "Acessibilidade")
        Spacer(modifier = Modifier.height(16.dp))
        ProfileMenuItem(icon = Icons.Filled.HelpOutline, text = "Ajuda")

        Spacer(modifier = Modifier.weight(1f))

        // Botão de Sair (Opcional)
        TextButton(
            onClick = {
                sessionManager.logout()
                // Aqui você precisaria navegar de volta para o Login,
                // mas depende de como sua navegação na MainActivity está configurada.
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Sair da conta", color = Color.Red)
        }
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 12.dp) // Aumentei o padding para melhor toque (Acessibilidade)
            .semantics(mergeDescendants = true) { }, // Lê o ícone e texto juntos
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null, // Decorativo, o texto já explica
            tint = PurplePrimary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}