package com.example.gradua.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gradua.ui.GraduaTextField
import com.example.gradua.ui.theme.PurplePrimary
import com.example.gradua.viewModel.FilterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    onFilterApplied: (String?, String, String) -> Unit,
    viewModel: FilterViewModel = viewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Estados locais
    var selectedSubject by remember { mutableStateOf("") }
    var selectedContent by remember { mutableStateOf("") }
    var searchText by remember { mutableStateOf("") }

    var expandedSubject by remember { mutableStateOf(false) }
    var expandedContent by remember { mutableStateOf(false) }

    val isContentEnabled = selectedSubject.isNotEmpty()

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            Surface(color = PurplePrimary, shadowElevation = 4.dp) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(PurplePrimary)
                            .windowInsetsTopHeight(WindowInsets.statusBars)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(PurplePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Filtro de Questões",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { paddingValues ->

        // --- MUDANÇA: Usamos Box para permitir centralizar o Loading ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
        ) {

            if (viewModel.isLoading) {
                // --- LOADING PADRONIZADO (Igual Perfil/Favoritos) ---
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = PurplePrimary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Carregando...",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            } else {
                // --- CONTEÚDO DA TELA ---
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        // Detecta toque no fundo para limpar foco
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                focusManager.clearFocus()
                            })
                        }
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        "Personalize seu estudo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- DROPDOWN MATÉRIA ---
                    ExposedDropdownMenuBox(
                        expanded = expandedSubject,
                        onExpandedChange = { expandedSubject = !expandedSubject }
                    ) {
                        OutlinedTextField(
                            value = selectedSubject,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Selecione a Matéria") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSubject) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),

                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                disabledContainerColor = Color.White,
                                focusedBorderColor = PurplePrimary,
                                unfocusedBorderColor = Color.Transparent,
                                focusedLabelColor = PurplePrimary,
                                unfocusedLabelColor = Color.Gray,
                                cursorColor = PurplePrimary,
                                focusedTrailingIconColor = PurplePrimary,
                                unfocusedTrailingIconColor = Color.Gray,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expandedSubject,
                            onDismissRequest = { expandedSubject = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            viewModel.availableSubjects.forEach { subject ->
                                DropdownMenuItem(
                                    text = { Text(subject, color = Color.Black) },
                                    onClick = {
                                        selectedSubject = subject
                                        selectedContent = ""
                                        viewModel.onSubjectSelected(subject)
                                        expandedSubject = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- DROPDOWN CONTEÚDO ---
                    ExposedDropdownMenuBox(
                        expanded = expandedContent,
                        onExpandedChange = {
                            if (isContentEnabled) expandedContent = !expandedContent
                        }
                    ) {
                        OutlinedTextField(
                            value = selectedContent,
                            onValueChange = {},
                            readOnly = true,
                            enabled = isContentEnabled,
                            label = { Text("Selecione o Conteúdo") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedContent) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),

                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                disabledContainerColor = Color(0xFFE0E0E0),
                                focusedBorderColor = PurplePrimary,
                                unfocusedBorderColor = Color.Transparent,
                                disabledBorderColor = Color.Transparent,
                                focusedLabelColor = PurplePrimary,
                                unfocusedLabelColor = Color.Gray,
                                disabledLabelColor = Color.Gray,
                                cursorColor = PurplePrimary,
                                focusedTrailingIconColor = PurplePrimary,
                                unfocusedTrailingIconColor = Color.Gray,
                                disabledTrailingIconColor = Color.Gray,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                disabledTextColor = Color.Gray
                            )
                        )
                        if (isContentEnabled) {
                            ExposedDropdownMenu(
                                expanded = expandedContent,
                                onDismissRequest = { expandedContent = false },
                                modifier = Modifier.background(Color.White)
                            ) {
                                viewModel.availableContents.forEach { content ->
                                    DropdownMenuItem(
                                        text = { Text(content, color = Color.Black) },
                                        onClick = {
                                            selectedContent = content
                                            expandedContent = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- CAMPO DE BUSCA ---
                    GraduaTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        placeholder = "Buscar por palavra-chave (Ex: DNA)"
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // --- BOTÃO FILTRAR ---
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            onFilterApplied(
                                if (selectedSubject.isEmpty() || selectedSubject == "Todas") null else selectedSubject,
                                searchText,
                                if (selectedContent.isEmpty() || selectedContent == "Todos") "" else selectedContent
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Filtrar Questões", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}