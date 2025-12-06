package com.example.gradua.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gradua.data.DatabaseHelper
import com.example.gradua.ui.GraduaTextField
import com.example.gradua.ui.theme.PurplePrimary
import com.example.gradua.utils.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onBackClick: () -> Unit, onRegisterSuccess: () -> Unit) {
    val context = LocalContext.current
    val dbHelper = remember { DatabaseHelper(context) }
    val sessionManager = remember { SessionManager(context) }

    var nome by remember { mutableStateOf("") }
    var escola by remember { mutableStateOf("") } // Alterado de telefone para escola
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var selectedExam by remember { mutableStateOf("ENEM") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Voltar", fontSize = 16.sp) }, // Texto auxiliar para acessibilidade visual
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar para a tela de login" // Acessibilidade
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.MenuBook,
                contentDescription = null, // Decorativo, já tem texto abaixo
                modifier = Modifier.size(60.dp),
                tint = PurplePrimary.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Criar conta",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.semantics { heading() } // Indica que é um título para leitores de tela
            )
            Spacer(modifier = Modifier.height(24.dp))

            GraduaTextField(value = nome, onValueChange = { nome = it }, placeholder = "Nome Completo", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))

            // Campo Escola
            GraduaTextField(
                value = escola,
                onValueChange = { escola = it },
                placeholder = "Nome da Escola",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            GraduaTextField(value = email, onValueChange = { email = it }, placeholder = "Email", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))

            GraduaTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Senha",
                isPassword = true,
                isPasswordVisible = isPasswordVisible,
                onVisibilityChange = { isPasswordVisible = !isPasswordVisible },
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Qual é o seu foco?",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier.semantics { heading() }
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Opções de acessibilidade para RadioButton
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("ENEM", "ENADE").forEach { exam ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .selectable(
                                selected = (selectedExam == exam),
                                onClick = { selectedExam = exam },
                                role = Role.RadioButton
                            )
                            .padding(8.dp)
                    ) {
                        RadioButton(
                            selected = (selectedExam == exam),
                            onClick = null, // O clique é tratado no Row para aumentar a área de toque
                            colors = RadioButtonDefaults.colors(selectedColor = PurplePrimary)
                        )
                        Text(
                            text = exam,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (nome.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                        val success = dbHelper.addUser(nome, escola, email, password, selectedExam)
                        if (success) {
                            sessionManager.saveUserSession(email) // Salva a sessão!
                            Toast.makeText(context, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()
                            onRegisterSuccess()
                        } else {
                            Toast.makeText(context, "Erro ao cadastrar.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .semantics { contentDescription = "Botão para finalizar cadastro e criar conta" },
                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Criar conta", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}