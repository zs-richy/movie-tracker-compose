package hu.szandras.cmovietracker.dao

import androidx.room.*
import hu.szandras.cmovietracker.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): MovieEntity?

    @Insert
    fun insertMovie(movie: MovieEntity)

    @Update
    fun updateMovie(movie: MovieEntity)

    @Delete
    fun deleteMovie(movie: MovieEntity)
}