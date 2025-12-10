package com.example.gradua.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gradua.data.UserStore
import com.example.gradua.ui.GraduaTextField
import com.example.gradua.ui.theme.PurplePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onBackClick: () -> Unit, onRegisterSuccess: () -> Unit) {
    val context = LocalContext.current

    // Inicializa o UserStore (Substituto do banco SQL)
    val userStore = remember { UserStore(context) }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Criar Conta") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Bem-vindo(a)!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PurplePrimary
            )

            Spacer(modifier = Modifier.height(32.dp))

            GraduaTextField(value = name, onValueChange = { name = it }, placeholder = "Nome Completo")
            Spacer(modifier = Modifier.height(16.dp))
            GraduaTextField(value = email, onValueChange = { email = it }, placeholder = "Email")
            Spacer(modifier = Modifier.height(16.dp))
            GraduaTextField(value = password, onValueChange = { password = it }, placeholder = "Senha", isPassword = true)

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                        // AGORA PASSAMOS A SENHA TAMBÉM
                        userStore.registerUser(name, email, password)

                        Toast.makeText(context, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()
                        onRegisterSuccess()
                    } else {
                        Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cadastrar", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}