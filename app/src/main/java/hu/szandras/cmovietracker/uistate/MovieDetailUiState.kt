package hu.szandras.cmovietracker.uistate

import hu.szandras.cmovietracker.entity.MovieEntity

data class MovieDetailUiState(
    val isFetching: Boolean = false,
    val isSaveCompleted: Boolean = false,
    val currentMovieId: Int = -1,
    val movie: MovieEntity? = null
)