package com.example.gradua.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DebugScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current

    // Listas para guardar os nomes dos arquivos encontrados
    var rootFiles by remember { mutableStateOf<List<String>>(emptyList()) }
    var imagesFiles by remember { mutableStateOf<List<String>>(emptyList()) }
    var lowercaseFiles by remember { mutableStateOf<List<String>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        try {
            // 1. Lista a raiz da pasta Assets
            rootFiles = context.assets.list("")?.toList() ?: listOf("Erro: Retornou nulo")

            // 2. Tenta listar a pasta "Imagens" (Maiúsculo)
            imagesFiles = context.assets.list("Imagens")?.toList() ?: listOf("Pasta 'Imagens' não encontrada")

            // 3. Tenta listar a pasta "imagens" (Minúsculo)
            lowercaseFiles = context.assets.list("imagens")?.toList() ?: listOf("Pasta 'imagens' não encontrada")

        } catch (e: Exception) {
            errorMessage = e.message ?: "Erro desconhecido"
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = onBackClick) { Text("Voltar") }

        Text("DIAGNÓSTICO DE ARQUIVOS", fontSize = 20.sp, color = Color.Red)
        if (errorMessage.isNotEmpty()) Text("Erro: $errorMessage", color = Color.Red)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            item {
                Text("--- Raiz de Assets ---",  color = Color.Blue)
            }
            items(rootFiles) { file ->
                Text("- $file", fontSize = 14.sp)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("--- Pasta 'Imagens' (Com I maiúsculo) ---", color = Color.Magenta)
            }
            if (imagesFiles.isEmpty()) {
                item { Text(" (Pasta vazia ou inexistente) ", color = Color.Red) }
            } else {
                items(imagesFiles) { file ->
                    Text("- $file", fontSize = 14.sp, color = Color(0xFF006400))
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("--- Pasta 'imagens' (Com i minúsculo) ---", color = Color.Magenta)
            }
            if (lowercaseFiles.isEmpty()) {
                item { Text(" (Pasta vazia ou inexistente) ", color = Color.Red) }
            } else {
                items(lowercaseFiles) { file ->
                    Text("- $file", fontSize = 14.sp)
                }
            }
        }
    }
}