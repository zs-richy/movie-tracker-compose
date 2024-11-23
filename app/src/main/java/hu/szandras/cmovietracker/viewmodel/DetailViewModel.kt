package hu.szandras.cmovietracker.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.szandras.cmovietracker.database.MovieRepository
import hu.szandras.cmovietracker.uistate.MovieDetailUiState
import hu.szandras.cmovietracker.uistate.MovieEditorUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState: MutableStateFlow<MovieDetailUiState> =
        MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> get() = _uiState

    private val movieIdToShow: Int = Integer.parseInt(savedStateHandle["movieId"] ?: "-1")

    init {
        if (movieIdToShow != -1) {
            fetchMovieEntityById(movieIdToShow)
        }
    }

    private fun fetchMovieEntityById(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isFetching = true)

            delay(1000)
            val movieEntity = withContext(Dispatchers.IO) {
                repository.getMovieById(id)
            }

            movieEntity?.let { entityToShow ->
                _uiState.value = _uiState.value.copy(
                    movie = entityToShow
                )
            }

            _uiState.value = _uiState.value.copy(isFetching = false)

        }
    }

}