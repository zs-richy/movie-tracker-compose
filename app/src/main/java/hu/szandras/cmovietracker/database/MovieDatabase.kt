package hu.szandras.cmovietracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import hu.szandras.cmovietracker.dao.MovieDao
import hu.szandras.cmovietracker.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}