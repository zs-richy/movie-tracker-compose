package hu.szandras.cmovietracker.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.szandras.cmovietracker.database.MovieRepository
import hu.szandras.cmovietracker.entity.MovieEntity
import hu.szandras.cmovietracker.uistate.MovieEditorUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MovieEditorUiState> =
        MutableStateFlow(MovieEditorUiState())
    val uiState: StateFlow<MovieEditorUiState> get() = _uiState

    private val movieIdToEdit: Int = Integer.parseInt(savedStateHandle["movieId"] ?: "-1")

    init {
        if (movieIdToEdit != -1) {
            fetchMovieEntityById(movieIdToEdit)
        }
    }

    private fun validateFields() {
        validateTitle()
        validateGenre()
        validateScore()
    }

    fun updateTitle(newValue: String) {
        _uiState.value = uiState.value.copy(
            title = newValue,
            validationState = uiState.value.validationState.copy(
                isTitleValid = newValue.isNotEmpty()
            )
        )
        validateTitle()
    }

    private fun validateTitle() {
        _uiState.value = uiState.value.copy(
            validationState = uiState.value.validationState.copy(
                isTitleValid = uiState.value.title?.isNotEmpty() ?: false
            )
        )
    }

    fun updateGenre(newValue: String) {
        _uiState.value = uiState.value.copy(
            genre = newValue,
        )
        validateGenre()
    }

    private fun validateGenre() {
        _uiState.value = uiState.value.copy(
            validationState = uiState.value.validationState.copy(
                isGenreValid = uiState.value.genre?.isNotEmpty() ?: false
            )
        )
    }

    fun updateDate(newValue: Long?) {
        _uiState.value = uiState.value.copy(
            date = newValue
        )
    }

    fun updateScore(newValue: String) {
        val parsedValue = try {
            Integer.parseInt(newValue)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        _uiState.value = uiState.value.copy(
            score = parsedValue
        )

        validateScore()
    }

    private fun validateScore() {
        _uiState.value = uiState.value.copy(
            validationState = uiState.value.validationState.copy(
                isScoreValid = uiState.value.score != null
            )
        )
    }

    private fun movieDetailsToMovieEntity(withId: Boolean = false): MovieEntity {
        if (withId) {
            return MovieEntity(
                id = movieIdToEdit,
                title = _uiState.value.title ?: "",
                genre = _uiState.value.genre ?: "",
                date = _uiState.value.date ?: 0L,
                score = _uiState.value.score ?: 0,
            )
        } else {
            return MovieEntity(
                title = _uiState.value.title ?: "",
                genre = _uiState.value.genre ?: "",
                date = _uiState.value.date ?: 0L,
                score = _uiState.value.score ?: 0,
            )
        }
    }

    private fun fetchMovieEntityById(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isFetching = true)

            delay(1000)
            val entityToEdit = withContext(Dispatchers.IO) {
                repository.getMovieById(id)
            }

            entityToEdit?.let { oldEntity ->
                _uiState.value = _uiState.value.copy(
                    title = oldEntity.title,
                    genre = oldEntity.genre,
                    date = oldEntity.date,
                    score = oldEntity.score,
                    currentMovieId = movieIdToEdit
                )
            }

            validateFields()

            _uiState.value = _uiState.value.copy(isFetching = false)
        }
    }

    fun addMovie() {
        viewModelScope.launch {
            val newMovie = movieDetailsToMovieEntity()

            _uiState.value = _uiState.value.copy(isFetching = true)
            delay(1000) // Simulate network delay

            withContext(Dispatchers.IO) {
                repository.insertMovie(newMovie)
            }

            _uiState.value = _uiState.value.copy(
                isFetching = false,
                isSaveCompleted = true
            )
        }
    }

    fun updateMovie() {
        viewModelScope.launch {
            val movieEntity = movieDetailsToMovieEntity(true)

            _uiState.value = _uiState.value.copy(isFetching = true)
            delay(1000) // Simulate network delay

            withContext(Dispatchers.IO) {
                repository.updateMovie(movieEntity)
            }

            _uiState.value = _uiState.value.copy(
                isSaveCompleted = true
            )
        }
    }

    fun deleteMovie() {
        viewModelScope.launch {
            val movieEntity = movieDetailsToMovieEntity(true)

            _uiState.value = _uiState.value.copy(isFetching = true)
            delay(1000) // Simulate network delay

            withContext(Dispatchers.IO) {
                repository.deleteMovie(movieEntity)
            }

            _uiState.value = _uiState.value.copy(
                isSaveCompleted = true
            )
        }
    }
}