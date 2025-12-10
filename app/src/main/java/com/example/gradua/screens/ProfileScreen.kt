package com.example.gradua.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gradua.ui.theme.PurplePrimary

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Filled.Person, contentDescription = null, modifier = Modifier.size(100.dp), tint = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Meu Perfil", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onLogout, // A lógica de limpar o UserStore está na MainActivity
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sair da Conta")
        }
    }
}