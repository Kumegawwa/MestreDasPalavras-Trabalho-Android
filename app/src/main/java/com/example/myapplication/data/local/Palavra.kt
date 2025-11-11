package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "palavras")
data class Palavra(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val palavra: String
)