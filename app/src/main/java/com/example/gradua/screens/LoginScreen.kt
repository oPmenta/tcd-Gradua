package com.example.gradua.screens

import com.example.gradua.ui.theme.PurplePrimary
import com.example.gradua.ui.theme.TextGray


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gradua.ui.GraduaTextField
import com.example.gradua.ui.theme.GraduaTheme

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onRegisterClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.MenuBook,
            contentDescription = "Logo",
            modifier = Modifier.size(80.dp),
            tint = PurplePrimary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Gradua", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)

        Spacer(modifier = Modifier.height(48.dp))

        Text("Faça seu Login", modifier = Modifier.align(Alignment.Start), fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(16.dp))

        GraduaTextField(value = email, onValueChange = { email = it }, placeholder = "Email")
        Spacer(modifier = Modifier.height(8.dp))
        GraduaTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Senha",
            isPassword = true,
            isPasswordVisible = isPasswordVisible,
            onVisibilityChange = { isPasswordVisible = !isPasswordVisible }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(checkedColor = PurplePrimary)
                )
                Text("Lembrar-se", fontSize = 14.sp)
            }
            TextButton(onClick = { /* Ação Esqueci Senha */ }) {
                Text("Esqueceu a senha?", color = PurplePrimary, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onLoginSuccess,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Login", fontSize = 18.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(" OU ", color = TextGray, modifier = Modifier.padding(horizontal = 8.dp))
            HorizontalDivider(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text("Ainda não tem uma conta? ")
            Text(
                "Cadastre-se",
                color = PurplePrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onRegisterClick() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    GraduaTheme {
        LoginScreen(
            onLoginSuccess = { },
            onRegisterClick = { }
        )
    }
}