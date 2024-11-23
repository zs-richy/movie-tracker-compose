package hu.szandras.cmovietracker.uistate

import hu.szandras.cmovietracker.viewmodel.EditorValidationState

data class MovieEditorUiState(
    val isFetching: Boolean = false,
    val isSaveCompleted: Boolean = false,
    val currentMovieId: Int = -1,
    val title: String? = null,
    val genre: String? = null,
    val date: Long? = System.currentTimeMillis(),
    val score: Int? = null,

    val validationState: EditorValidationState = EditorValidationState()
)


