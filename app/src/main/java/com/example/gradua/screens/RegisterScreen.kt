package com.example.gradua.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gradua.R
import com.example.gradua.data.DatabaseHelper
import com.example.gradua.screens.SelectionCard
import com.example.gradua.ui.GraduaTextField
import com.example.gradua.ui.theme.PurplePrimary
import com.example.gradua.ui.theme.TextGray
import com.example.gradua.utils.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onBackClick: () -> Unit, onRegisterSuccess: () -> Unit) {
    val context = LocalContext.current
    val dbHelper = remember { DatabaseHelper(context) }
    val sessionManager = remember { SessionManager(context) }

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    var escola by remember { mutableStateOf("") }
    var faculdade by remember { mutableStateOf("") }

    var selectedExam by remember { mutableStateOf("ENEM") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Voltar", fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Gradua",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = "Cadastro",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.semantics { heading() }
            )
            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                GraduaTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    placeholder = "Nome Completo",
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                GraduaTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email",
                    fontSize = 16.sp
                )

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
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Cartão do ENEM
                SelectionCard(
                    title = "ENEM",
                    description = "Ensino Médio",
                    icon = Icons.AutoMirrored.Outlined.MenuBook,
                    isSelected = selectedExam == "ENEM",
                    onClick = { selectedExam = "ENEM" },
                    modifier = Modifier.weight(1f) // Ocupa 50% do espaço
                )

                // Cartão do ENADE
                SelectionCard(
                    title = "ENADE",
                    description = "Graduação",
                    icon = Icons.Outlined.School,
                    isSelected = selectedExam == "ENADE",
                    onClick = { selectedExam = "ENADE" },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp) // O mesmo padding dos outros
            ) {
                if (selectedExam == "ENEM") {
                    GraduaTextField(
                        value = escola,
                        onValueChange = { escola = it },
                        placeholder = "Escola",
                        fontSize = 16.sp
                    )
                } else {
                    GraduaTextField(
                        value = faculdade,
                        onValueChange = { faculdade = it },
                        placeholder = "Faculdade",
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Lógica para pegar o dado correto
                    val instituicaoEscolhida = if (selectedExam == "ENEM") escola else faculdade

                    if (nome.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && instituicaoEscolhida.isNotEmpty()) {
                        val success = dbHelper.addUser(nome, instituicaoEscolhida, email, password, selectedExam)
                        if (success) {
                            sessionManager.saveUserSession(email)
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
                    .padding(horizontal = 16.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Criar conta", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}

// função dos botões ENEM e ENADE
@Composable
fun SelectionCard(
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (isSelected) PurplePrimary else Color.LightGray // Define a cor uma vez só

    OutlinedCard(
        onClick = onClick,
        modifier = modifier.height(110.dp), // Removi fillMaxWidth aqui para deixar flexivel
        border = BorderStroke(2.dp, color),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically) // Substitui os Spacers
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(30.dp))

            Text(text = title, fontWeight = FontWeight.Bold, color = if (isSelected) PurplePrimary else Color.Gray)

            Text(text = description, fontSize = 12.sp, color = TextGray)
        }
    }
}