package com.example.gradua.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gradua.R
import com.example.gradua.data.UserStore
import com.example.gradua.network.GraduaServerApi
import com.example.gradua.network.LoginRequest
import com.example.gradua.ui.GraduaTextField
import com.example.gradua.ui.theme.PurplePrimary
import com.example.gradua.ui.theme.TextGray
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onRegisterClick: () -> Unit) {
    val context = LocalContext.current
    val userStore = remember { UserStore(context) }
    val scope = rememberCoroutineScope()

    // 1. Gerenciador de Foco
    val focusManager = LocalFocusManager.current

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            // 2. Detecta toque no fundo para tirar o foco
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        GraduaTextField(
            value = username,
            onValueChange = { username = it },
            placeholder = "Nome de Usuário"
        )

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

        if (isLoading) {
            CircularProgressIndicator(color = PurplePrimary)
        } else {
            Button(
                onClick = {
                    // Tira o foco ao clicar no botão também
                    focusManager.clearFocus()

                    if (username.isNotEmpty() && password.isNotEmpty()) {
                        isLoading = true

                        scope.launch {
                            try {
                                val request = LoginRequest(username, password)
                                val response = GraduaServerApi.service.loginUser(request)

                                if (response.userId != null) {
                                    userStore.saveUserSession(
                                        userId = response.userId.toString(),
                                        username = response.username ?: username
                                    )
                                    onLoginSuccess()
                                } else {
                                    Toast.makeText(context, response.erro ?: "Login falhou", Toast.LENGTH_LONG).show()
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
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Entrar", fontSize = 18.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Não tem uma conta? ", color = TextGray)
            Text(
                text = "Cadastre-se",
                color = PurplePrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onRegisterClick() }
            )
        }
    }
}