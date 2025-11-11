package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PalavraDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(palavra: Palavra)

    @Update
    suspend fun update(palavra: Palavra)

    @Delete
    suspend fun delete(palavra: Palavra)

    @Query("SELECT * FROM palavras ORDER BY palavra ASC")
    fun getAllFlow(): Flow<List<Palavra>>

    @Query("SELECT * FROM palavras ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomWord(): Palavra?

    @Query("SELECT * FROM palavras WHERE palavra = :palavra LIMIT 1")
    suspend fun getWordByString(palavra: String): Palavra?

    @Query("DELETE FROM palavras")
    suspend fun nukeTable()
}