package hu.szandras.cmovietracker.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val genre: String,
    val date: Long,
    val score: Int
)