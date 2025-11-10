package com.example.gradua.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gradua.ui.GraduaTextField
import com.example.gradua.ui.theme.PurplePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onBackClick: () -> Unit, onRegisterSuccess: () -> Unit) {
    var nome by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
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
                contentDescription = "Logo",
                modifier = Modifier.size(60.dp),
                tint = PurplePrimary.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text("Criar conta", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(32.dp))

            GraduaTextField(value = nome, onValueChange = { nome = it }, placeholder = "Nome")
            Spacer(modifier = Modifier.height(16.dp))
            GraduaTextField(value = telefone, onValueChange = { telefone = it }, placeholder = "Número de telefone")
            Spacer(modifier = Modifier.height(16.dp))
            GraduaTextField(value = email, onValueChange = { email = it }, placeholder = "Email")
            Spacer(modifier = Modifier.height(16.dp))
            GraduaTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Senha",
                isPassword = true,
                isPasswordVisible = isPasswordVisible,
                onVisibilityChange = { isPasswordVisible = !isPasswordVisible }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onRegisterSuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Criar conta", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}