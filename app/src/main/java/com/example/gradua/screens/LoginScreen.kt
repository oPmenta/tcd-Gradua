package com.example.gradua.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.gradua.ui.GraduaTextField
import com.example.gradua.ui.theme.PurplePrimary
import com.example.gradua.ui.theme.TextGray

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onRegisterClick: () -> Unit) {
    val context = LocalContext.current
    val userStore = remember { UserStore(context) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ... (Seu código de Logo e Textos permanece igual) ...
        Image(painter = painterResource(id = R.drawable.logo), contentDescription = null, modifier = Modifier.size(150.dp))
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

        // BOTÃO ENTRAR CORRIGIDO
        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    // VERIFICAÇÃO REAL
                    val isValid = userStore.validateLogin(email, password)

                    if (isValid) {
                        // Se deu certo, re-salva que está logado e entra
                        userStore.registerUser(userStore.getUserName(), email, password)
                        onLoginSuccess()
                    } else {
                        Toast.makeText(context, "Email ou senha incorretos. Cadastre-se primeiro.", Toast.LENGTH_LONG).show()
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