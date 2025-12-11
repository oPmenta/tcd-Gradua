package com.example.gradua.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gradua.data.UserStore
import com.example.gradua.network.GraduaServerApi
import com.example.gradua.network.RegisterRequest
import com.example.gradua.ui.GraduaTextField
import com.example.gradua.ui.theme.PurplePrimary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onBackClick: () -> Unit, onRegisterSuccess: () -> Unit) {
    val context = LocalContext.current
    val userStore = remember { UserStore(context) }
    val scope = rememberCoroutineScope()

    // 1. Gerenciador de Foco
    val focusManager = LocalFocusManager.current

    var username by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var school by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFFFDFDFD),
        contentWindowInsets = WindowInsets(0.dp)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                // 2. Detecta toque no fundo
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Cabeçalho
            Surface(
                color = Color.Transparent,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .height(56.dp)
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.Black
                        )
                    }

                    Text(
                        text = "Criar Conta",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                "Bem-vindo(a)!",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = PurplePrimary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Formulário
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {

                GraduaTextField(value = username, onValueChange = { username = it }, placeholder = "Nome de Usuário")
                Spacer(modifier = Modifier.height(16.dp))

                GraduaTextField(value = fullName, onValueChange = { fullName = it }, placeholder = "Nome Completo")
                Spacer(modifier = Modifier.height(16.dp))

                GraduaTextField(value = school, onValueChange = { school = it }, placeholder = "Escola")
                Spacer(modifier = Modifier.height(16.dp))

                GraduaTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Senha",
                    isPassword = true,
                    isPasswordVisible = isPasswordVisible,
                    onVisibilityChange = { isPasswordVisible = !isPasswordVisible }
                )

                Spacer(modifier = Modifier.height(40.dp))

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = PurplePrimary)
                    }
                } else {
                    Button(
                        onClick = {
                            focusManager.clearFocus() // Tira foco ao clicar

                            if (username.isNotEmpty() && fullName.isNotEmpty() &&
                                school.isNotEmpty() && password.isNotEmpty()) {

                                isLoading = true

                                scope.launch {
                                    try {
                                        val request = RegisterRequest(
                                            username = username,
                                            senha = password,
                                            nomeCompleto = fullName,
                                            escola = school
                                        )

                                        val response = GraduaServerApi.service.registerUser(request)

                                        if (response.userId != null) {
                                            userStore.saveUserSession(
                                                userId = response.userId.toString(),
                                                username = response.username ?: username
                                            )
                                            Toast.makeText(context, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()
                                            onRegisterSuccess()
                                        } else {
                                            Toast.makeText(context, response.erro ?: "Erro no cadastro", Toast.LENGTH_LONG).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Erro de conexão: ${e.message}", Toast.LENGTH_LONG).show()
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Cadastrar", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}