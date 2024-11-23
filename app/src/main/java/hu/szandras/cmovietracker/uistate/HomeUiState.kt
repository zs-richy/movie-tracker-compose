package hu.szandras.cmovietracker.uistate

import hu.szandras.cmovietracker.entity.MovieEntity

data class HomeUiState(
    val movies: List<MovieEntity> = emptyList(),
    val isLoading: Boolean = false,
)