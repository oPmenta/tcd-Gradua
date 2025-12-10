package com.example.gradua.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gradua.data.Question
import com.example.gradua.ui.theme.InputBg
import com.example.gradua.ui.theme.PurplePrimary
import com.example.gradua.ui.theme.TextGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraduaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onVisibilityChange: () -> Unit = {},
    fontSize: TextUnit = 14.sp
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = TextGray, fontSize = fontSize) },
        modifier = modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = InputBg,
            unfocusedContainerColor = InputBg,
            disabledContainerColor = InputBg,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default,
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = onVisibilityChange) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (isPasswordVisible) "Ocultar senha" else "Mostrar senha",
                        tint = TextGray
                    )
                }
            }
        },
        singleLine = true
    )
}

@Composable
fun QuestionItem(
    question: Question,
    selectedOption: String?,
    showFeedback: Boolean,
    isFavorite: Boolean, // Recebe se é favorito
    onFavoriteToggle: () -> Unit, // Função para favoritar/desfavoritar
    onOptionSelected: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // CABEÇALHO (Matéria + ID + Coração)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically // Alinha verticalmente no centro
            ) {
                // Matéria na Esquerda
                Text(
                    text = question.materia,
                    color = PurplePrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                // ID e Coração na Direita
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "ID: ${question.remoteId}",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(end = 8.dp) // Espaço entre ID e Coração
                    )

                    // Ícone de Favoritar
                    IconToggleButton(
                        checked = isFavorite,
                        onCheckedChange = { onFavoriteToggle() },
                        modifier = Modifier.size(24.dp) // Tamanho do ícone
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favoritar",
                            tint = if (isFavorite) Color.Red else Color.Gray // Vermelho se marcado
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // TEXTO (Mantido Justificado e Hifenizado como você pediu)
            val enunciadoFormatado = question.enunciado.replace("\t", "    ")

            Text(
                text = enunciadoFormatado,
                fontSize = 16.sp,
                color = Color.Black,
                lineHeight = 24.sp,
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium.copy(
                    hyphens = Hyphens.Auto,
                    lineBreak = LineBreak.Paragraph
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // IMAGEM
            if (!question.imagemPath.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(question.imagemPath)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagem da questão",
                    modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp),
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center,
                    error = rememberVectorPainter(Icons.Default.BrokenImage)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ALTERNATIVAS
            question.alternativas.forEach { alt ->
                AlternativeButton(
                    letter = alt.letra,
                    text = alt.texto,
                    isSelected = selectedOption == alt.letra,
                    isCorrectAnswer = question.gabarito == alt.letra,
                    showFeedback = showFeedback,
                    onClick = { if (!showFeedback) onOptionSelected(alt.letra) }
                )
            }
        }
    }
}

@Composable
fun AlternativeButton(
    letter: String,
    text: String,
    isSelected: Boolean,
    isCorrectAnswer: Boolean,
    showFeedback: Boolean,
    onClick: () -> Unit
) {
    // CORES (Mantidas conforme sua última aprovação)
    val backgroundColor = when {
        showFeedback && isCorrectAnswer -> Color(0xFF4CAF50)
        showFeedback && isSelected && !isCorrectAnswer -> Color(0xFFE53935)
        isSelected && !showFeedback -> PurplePrimary.copy(alpha = 0.1f)
        else -> Color.Transparent
    }

    val borderColor = when {
        showFeedback && isCorrectAnswer -> Color(0xFF4CAF50)
        showFeedback && isSelected && !isCorrectAnswer -> Color(0xFFE53935)
        isSelected -> PurplePrimary
        else -> Color.LightGray
    }

    val contentColor = when {
        showFeedback && isCorrectAnswer -> Color.White
        showFeedback && isSelected && !isCorrectAnswer -> Color.White
        isSelected && !showFeedback -> PurplePrimary
        else -> Color.Black
    }

    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(if (isSelected || (showFeedback && isCorrectAnswer)) 2.dp else 1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "$letter)",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 22.sp,
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium.copy(
                    hyphens = Hyphens.Auto,
                    lineBreak = LineBreak.Paragraph
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}