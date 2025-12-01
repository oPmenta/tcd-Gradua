package com.example.gradua.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star

import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.FilterList

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.gradua.ui.theme.InputBg
import com.example.gradua.ui.theme.PurplePrimary
import com.example.gradua.ui.theme.TextGray

// --- Se as cores não estiverem sendo reconhecidas, descomente as linhas abaixo ---
// val PurplePrimary = Color(0xFF6C63FF)
// val TextGray = Color(0xFF888888)
// val InputBg = Color(0xFFF5F5F5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraduaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onVisibilityChange: (() -> Unit)? = null,
    fontSize: TextUnit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = TextGray) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = InputBg,
            unfocusedContainerColor = InputBg,
            disabledContainerColor = InputBg,
            focusedBorderColor = PurplePrimary,
            unfocusedBorderColor = Color.Transparent,
        ),
        visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { onVisibilityChange?.invoke() }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "Alternar senha",
                        tint = TextGray
                    )
                }
            }
        } else null,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun GraduaBottomBar(selectedItem: Int, onNavigate: (Int) -> Unit) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            selected = selectedItem == 0,
            onClick = { onNavigate(0) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = PurplePrimary, indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Star, contentDescription = "Favoritos") },
            selected = selectedItem == 1,
            onClick = { onNavigate(1) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = PurplePrimary, indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.FilterList, contentDescription = "Filtrar") },
            selected = selectedItem == 2,
            onClick = { onNavigate(2) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = PurplePrimary, indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Usuário") },
            selected = selectedItem == 3,
            onClick = { onNavigate(3) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = PurplePrimary, indicatorColor = Color.Transparent)
        )
    }
}