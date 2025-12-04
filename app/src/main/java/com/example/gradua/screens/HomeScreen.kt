package com.example.gradua.screens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gradua.ui.theme.GraduaTheme
import com.example.gradua.ui.theme.PurplePrimary
import com.example.gradua.ui.theme.TextGray

@Composable
fun HomeScreen1() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("ENEM", color = PurplePrimary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
        }
        Spacer(modifier = Modifier.height(32.dp))
        Box(modifier = Modifier.height(150.dp), contentAlignment = Alignment.Center) {
            Icon(Icons.Filled.EmojiEvents, contentDescription = "Troféu", modifier = Modifier.size(100.dp), tint = Color(0xFFFFC107))
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { /* Simulado Diário */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Simulado Diário", fontSize = 16.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.height(32.dp))
        MenuItem(icon = Icons.Filled.EmojiEvents, text = "Ranking semanal", iconTint = Color(0xFFFF9800))
        Spacer(modifier = Modifier.height(16.dp))
        MenuItem(icon = Icons.Filled.Assignment, text = "Simulado personalizado", iconTint = Color(0xFF2196F3))
    }
}

@Composable
fun MenuItem(icon: ImageVector, text: String, iconTint: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GraduaTheme {
        HomeScreen1()
    }
}