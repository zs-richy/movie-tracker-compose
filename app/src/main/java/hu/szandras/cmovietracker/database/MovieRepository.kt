package hu.szandras.cmovietracker.database

import hu.szandras.cmovietracker.dao.MovieDao
import hu.szandras.cmovietracker.entity.MovieEntity
import javax.inject.Inject

class MovieRepository @Inject constructor(private val movieDao: MovieDao) {
    fun getAllMovies() = movieDao.getAllMovies()
    suspend fun getMovieById(id: Int) = movieDao.getMovieById(id)
    fun insertMovie(movie: MovieEntity) = movieDao.insertMovie(movie)
    fun updateMovie(movie: MovieEntity) = movieDao.updateMovie(movie)
    fun deleteMovie(movie: MovieEntity) = movieDao.deleteMovie(movie)
}