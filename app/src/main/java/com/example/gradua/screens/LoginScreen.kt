package com.example.gradua.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gradua.R
import com.example.gradua.data.UserStore
import com.example.gradua.network.GraduaServerApi
import com.example.gradua.network.LoginDto
import com.example.gradua.ui.GraduaTextField
import com.example.gradua.ui.theme.PurplePrimary
import com.example.gradua.ui.theme.TextGray
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onRegisterClick: () -> Unit) {
    val context = LocalContext.current
    val userStore = remember { UserStore(context) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
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

        Text(
            "Bem-vindo de volta!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = PurplePrimary
        )

        Spacer(modifier = Modifier.height(32.dp))

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
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    isLoading = true
                    scope.launch {
                        try {
                            // Envia login para o Python
                            val loginDto = LoginDto(email, password)
                            val response = GraduaServerApi.service.loginUser(loginDto)

                            // Verifica sucesso (se tem user_id, deu certo)
                            if (response.userId != null) {

                                // Salva no celular para manter a sessão
                                userStore.registerUser(
                                    name = response.nomeCompleto ?: "Estudante",
                                    username = response.username ?: "",
                                    email = email,
                                    password = password,
                                    // Obs: O Python não mandou escola, salvamos vazio ou texto padrão
                                    escola = response.escola ?: "Escola não informada"
                                )
                                // Marca logado
                                userStore.login(email)
                                onLoginSuccess()
                            } else {
                                Toast.makeText(context, response.erro ?: "Erro ao fazer login", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: retrofit2.HttpException) {
                            if (e.code() == 401) {
                                Toast.makeText(context, "Email ou senha incorretos!", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Erro no servidor: ${e.code()}", Toast.LENGTH_LONG).show()
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
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Entrar", fontSize = 18.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onRegisterClick() }.padding(8.dp)
        ) {
            Text("Não tem uma conta? ", color = TextGray)
            Text("Cadastre-se", color = PurplePrimary, fontWeight = FontWeight.Bold)
        }
    }
}