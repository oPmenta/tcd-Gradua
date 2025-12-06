package com.example.gradua.data

import androidx.room.Entity
import androidx.room.PrimaryKey
// NÃO deve ter import com.example.gradua.screens.Question aqui!

@Entity(tableName = "favorite_questions")
data class FavoriteQuestion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val originalId: Int,
    val title: String,
    val subject: String,
    val year: String,
    val text: String
)

// O sistema vai usar automaticamente o 'Question' que está no arquivo Models.kt (mesmo pacote)
fun Question.toFavorite() = FavoriteQuestion(
    originalId = this.id,
    title = this.title,
    subject = this.subject,
    year = this.year,
    text = this.text
)

fun FavoriteQuestion.toQuestion() = Question(
    id = this.originalId,
    title = this.title,
    subject = this.subject,
    year = this.year,
    text = this.text
)