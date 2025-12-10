package com.example.gradua.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager // IMPORTANTE
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gradua.ui.theme.PurplePrimary
import com.example.gradua.viewModel.FilterViewModel

@Composable
fun FilterScreen(
    // Agora aceita 3 parâmetros: Materia, Busca e CONTEUDO
    onFilterApplied: (String, String, String) -> Unit,
    viewModel: FilterViewModel = viewModel()
) {
    // Gerenciador de Foco para fechar teclado
    val focusManager = LocalFocusManager.current

    var searchText by remember { mutableStateOf("") }
    var selectedSubject by remember { mutableStateOf("") }
    var selectedContent by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            // --- AQUI ESTÁ O TRUQUE PARA O FOCO ---
            // Detecta toque em qualquer lugar vazio e tira o foco
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Filtro de Questões",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        if (viewModel.isLoading) {
            CircularProgressIndicator(color = PurplePrimary)
            Text("Carregando...", modifier = Modifier.padding(top = 8.dp))
        } else {

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar no enunciado") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            GraduaDropdown(
                label = "Selecione a Matéria",
                options = viewModel.availableSubjects,
                selectedOption = selectedSubject,
                onOptionSelected = { materia ->
                    selectedSubject = materia
                    selectedContent = ""
                    viewModel.onSubjectSelected(materia)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedSubject.isNotEmpty()) {
                GraduaDropdown(
                    label = "Selecione o Conteúdo",
                    options = viewModel.availableContents,
                    selectedOption = selectedContent,
                    onOptionSelected = { selectedContent = it }
                )
            } else {
                Text("Selecione uma matéria primeiro", fontSize = 14.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    // Passa os 3 filtros para a MainActivity
                    onFilterApplied(selectedSubject, searchText, selectedContent)
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Filtrar", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}

// (Mantenha o seu GraduaDropdown igual aqui embaixo...)
@Composable
fun GraduaDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val displayText = if (selectedOption.isEmpty()) label else selectedOption

    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { expanded = !expanded }
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = displayText, color = Color.Black)
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
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}