package com.example.gradua.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gradua.R
import com.example.gradua.data.DatabaseHelper
import com.example.gradua.ui.GraduaTextField
import com.example.gradua.ui.theme.PurplePrimary
import com.example.gradua.ui.theme.TextGray
import com.example.gradua.utils.SessionManager

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onRegisterClick: () -> Unit) {
    val context = LocalContext.current
    // Inicializa os helpers de Banco de Dados e Sessão
    val dbHelper = remember { DatabaseHelper(context) }
    val sessionManager = remember { SessionManager(context) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo com descrição decorativa (null) pois o texto abaixo já explica
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Gradua", // Descrição para acessibilidade
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Título e Subtítulo
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Gradua",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = PurplePrimary,
                modifier = Modifier.semantics { heading() } // Indica que é um Título
            )
            Text(
                text = "Seu app de estudos",
                fontSize = 16.sp,
                color = TextGray
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Campos de Texto
        GraduaTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Email",
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        GraduaTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Senha",
            isPassword = true,
            isPasswordVisible = isPasswordVisible,
            onVisibilityChange = { isPasswordVisible = !isPasswordVisible },
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Esqueceu senha
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Esqueceu sua senha?",
                color = PurplePrimary,
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable {
                        Toast.makeText(context, "Funcionalidade em breve", Toast.LENGTH_SHORT).show()
                    }
                    .semantics { role = Role.Button } // Avisa que é clicável como um botão
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botão Entrar
        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    val isValid = dbHelper.checkUser(email, password)
                    if (isValid) {
                        // Salva a sessão antes de navegar
                        sessionManager.saveUserSession(email)
                        onLoginSuccess()
                    } else {
                        Toast.makeText(context, "Email ou senha inválidos", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .semantics { contentDescription = "Botão Entrar na conta" },
            colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Entrar", fontSize = 18.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Rodapé Cadastro - Melhorado para acessibilidade
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onRegisterClick() }
                .padding(8.dp) // Aumenta área de toque
                .semantics(mergeDescendants = true) { // Lê tudo junto como uma frase única
                    contentDescription = "Não tem uma conta? Cadastre-se"
                    role = Role.Button
                }
        ) {
            Text("Não tem uma conta? ", color = TextGray)
            Text(
                text = "Cadastre-se",
                color = PurplePrimary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}