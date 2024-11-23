package hu.szandras.cmovietracker.viewmodel

data class EditorValidationState(
    val isTitleValid: Boolean = false,
    val isGenreValid: Boolean = false,
    val isScoreValid: Boolean = false,
)

fun EditorValidationState.isValid(): Boolean {
    return isTitleValid
            && isGenreValid
            && isScoreValid
}