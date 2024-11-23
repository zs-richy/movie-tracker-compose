package hu.szandras.cmovietracker.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.szandras.cmovietracker.dao.MovieDao
import hu.szandras.cmovietracker.database.MovieDatabase
import hu.szandras.cmovietracker.database.MovieRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMovieRepository(movieDao: MovieDao): MovieRepository {
        return MovieRepository(movieDao)
    }

    @Provides
    @Singleton
    fun provideMovieDao(database: MovieDatabase): MovieDao {
        return database.movieDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): MovieDatabase {
        return Room.databaseBuilder(appContext, MovieDatabase::class.java, "movies.db").build()
    }

}