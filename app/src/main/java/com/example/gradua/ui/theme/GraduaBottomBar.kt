package com.example.gradua.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.gradua.ui.theme.PurplePrimary

@Composable
fun GraduaBottomBar(
    selectedItem: Int,
    onNavigate: (Int) -> Unit // Usando o nome correto 'onNavigate'
) {
    // Lista dos itens do menu (Nome e Ícone)
    val items = listOf(
        BottomNavItem("Início", Icons.Filled.Home),
        BottomNavItem("Favoritos", Icons.Filled.Favorite),
        BottomNavItem("Filtrar", Icons.Filled.FilterList),
        BottomNavItem("Perfil", Icons.Filled.Person)
    )

    NavigationBar(
        containerColor = Color.White
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = selectedItem == index,
                onClick = { onNavigate(index) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PurplePrimary,
                    selectedTextColor = PurplePrimary,
                    indicatorColor = Color.Transparent, // Remove a bolha de seleção se quiser
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}

// Classe auxiliar simples para guardar os dados do item
data class BottomNavItem(
    val label: String,
    val icon: ImageVector
)