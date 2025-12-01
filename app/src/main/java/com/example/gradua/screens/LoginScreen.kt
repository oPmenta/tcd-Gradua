package com.example.gradua.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gradua.ui.GraduaTextField
import com.example.gradua.ui.theme.InputBg
import com.example.gradua.ui.theme.PurplePrimary
// O TextGray não é mais necessário aqui pois vamos usar Color.Black direto,
// mas mantive o import caso precise reverter no futuro.
import com.example.gradua.ui.theme.TextGray

@Composable
fun FilterScreen() {
    var searchText by remember { mutableStateOf("") }
    var selectedSubject by remember { mutableStateOf("") }
    var selectedContent by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Filtro de Questões",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo de texto
        GraduaTextField(
            value = searchText,
            onValueChange = { searchText = it },
            fontSize = 18.sp,
            colortext = Color.Black, // Garante que a digitação seja preta
            placeholder = "Digite uma parte do enunciado..."
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Dropdowns
        GraduaDropdown(
            label = "Selecione a Matéria",
            options = listOf("Matemática", "Português", "História", "Geografia"),
            selectedOption = selectedSubject,
            onOptionSelected = { selectedSubject = it }
        )
        Spacer(modifier = Modifier.height(16.dp))

        GraduaDropdown(
            label = "Selecione o Conteúdo",
            options = listOf("Álgebra", "Geometria", "Interpretação", "Gramática"),
            selectedOption = selectedContent,
            onOptionSelected = { selectedContent = it }
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Botão Filtrar
        Button(
            onClick = { /* Ação de Filtrar */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Filtrar", fontSize = 18.sp, color = Color.Black)
        }
    }
}

// Componente auxiliar para simular o Dropdown com o mesmo estilo do GraduaTextField
@Composable
fun GraduaDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val displayText = if (selectedOption.isEmpty()) label else selectedOption

    // ALTERAÇÃO: Força a cor preta sempre, removendo a lógica do cinza
    val textColor = Color.Black

    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = InputBg),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { expanded = !expanded }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = displayText, color = textColor)
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Expandir",
                    tint = Color.Black
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.85f) // Ajuste de largura se necessário
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        // Garante que o texto da opção também seja preto
                        Text(text = option, color = Color.Black)
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}