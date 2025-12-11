package com.example.gradua.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gradua.ui.theme.PurplePrimary
import com.example.gradua.viewModel.FilterViewModel

@Composable
fun FilterScreen(
    onFilterApplied: (String, String, String) -> Unit,
    viewModel: FilterViewModel = viewModel()
) {
    val focusManager = LocalFocusManager.current

    var searchText by remember { mutableStateOf("") }
    var selectedSubject by remember { mutableStateOf("") }
    var selectedContent by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
            // --- CORREÇÃO DE ALINHAMENTO AQUI ---
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = PurplePrimary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Carregando...", color = Color.Gray, fontSize = 16.sp)
                }
            }
        } else {

            // CAMPO DE BUSCA
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar no enunciado") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF0F0F0),
                    unfocusedContainerColor = Color(0xFFF0F0F0),
                    disabledContainerColor = Color(0xFFF0F0F0),
                    focusedBorderColor = PurplePrimary,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = PurplePrimary,
                    focusedLabelColor = PurplePrimary,
                    unfocusedLabelColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // SELECT MATÉRIA
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

            // SELECT CONTEÚDO
            if (selectedSubject.isNotEmpty()) {
                GraduaDropdown(
                    label = "Selecione o Conteúdo",
                    options = viewModel.availableContents,
                    selectedOption = selectedContent,
                    onOptionSelected = { selectedContent = it }
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(Color(0xFFF0F0F0), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Selecione uma matéria primeiro",
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
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

@Composable
fun GraduaDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val borderCol = if (expanded) PurplePrimary else Color.Transparent
    val labelCol = if (expanded) PurplePrimary else Color.Gray
    val iconCol = if (expanded) PurplePrimary else Color.Gray

    Box(modifier = Modifier.fillMaxWidth()) {

        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Expandir",
                    tint = iconCol
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF0F0F0),
                unfocusedContainerColor = Color(0xFFF0F0F0),
                disabledContainerColor = Color(0xFFF0F0F0),
                focusedBorderColor = borderCol,
                unfocusedBorderColor = borderCol,
                focusedLabelColor = labelCol,
                unfocusedLabelColor = labelCol,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    if (!expanded) {
                        expanded = true
                        focusRequester.requestFocus()
                    } else {
                        expanded = false
                        focusManager.clearFocus()
                    }
                }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                focusManager.clearFocus()
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color.White, RoundedCornerShape(12.dp))
                .heightIn(max = 250.dp),
            offset = androidx.compose.ui.unit.DpOffset(x = 0.dp, y = 4.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = if (option == selectedOption) PurplePrimary else Color.Black,
                            fontWeight = if (option == selectedOption) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.background(
                        if (option == selectedOption) PurplePrimary.copy(alpha = 0.05f) else Color.Transparent
                    )
                )
            }
        }
    }
}