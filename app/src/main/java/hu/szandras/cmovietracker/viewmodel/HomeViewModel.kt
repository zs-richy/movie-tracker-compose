package hu.szandras.cmovietracker.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.szandras.cmovietracker.database.MovieRepository
import hu.szandras.cmovietracker.entity.MovieEntity
import hu.szandras.cmovietracker.uistate.HomeUiState
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val movies: Flow<List<MovieEntity>> = repository.getAllMovies()
        .onStart {
            println("HomeViewModel: fetchingStarted")
            isLoading.value = true
        }
        .onEach {
            delay(750)
            isLoading.value = false
        }
        .onCompletion {
            isLoading.value = false
        }
        .catch {
            isLoading.value = false
        }

    private val _uiState = combine(isLoading, movies) { isLoading, movies ->
        HomeUiState(movies, isLoading)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(listOf(), true)
    )

    val uiState: StateFlow<HomeUiState> get() = _uiState


}