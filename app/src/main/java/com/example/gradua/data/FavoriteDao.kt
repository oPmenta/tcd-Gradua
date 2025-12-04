package com.example.gradua.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_questions")
    fun getAllFavorites(): Flow<List<FavoriteQuestion>>

    // Verifica se uma questão já é favorita pelo ID original
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_questions WHERE originalId = :originalId LIMIT 1)")
    fun isFavorite(originalId: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(question: FavoriteQuestion)

    @Query("DELETE FROM favorite_questions WHERE originalId = :originalId")
    suspend fun deleteFavoriteByOriginalId(originalId: Int)
}