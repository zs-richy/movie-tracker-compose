package hu.szandras.cmovietracker.screen

import android.icu.text.DateFormat
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import hu.szandras.cmovietracker.R
import hu.szandras.cmovietracker.component.editorscreen.ConfirmDeletionModal
import hu.szandras.cmovietracker.viewmodel.EditorViewModel
import hu.szandras.cmovietracker.viewmodel.isValid
import hu.szandras.cmovietracker.widget.DatePickerModal
import java.util.Date

@Composable
fun MovieEditorScreen(
    navController: NavController,
    movieId: Int?,
) {
    val editorViewModel: EditorViewModel = hiltViewModel()
    val uiState by editorViewModel.uiState.collectAsStateWithLifecycle()

    val showDateDialog = remember { mutableStateOf(false) }
    val showConfirmModal = remember { mutableStateOf(false) }

    val isEditing = remember {
        !(movieId == null || movieId == -1)
    }

    LaunchedEffect(uiState.isSaveCompleted) {
        if (uiState.isSaveCompleted) {
            navController.navigateUp()
        }
    }


    Box(
        modifier = Modifier.padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Text(
                text = if (isEditing) stringResource(R.string.edit_movie) else stringResource(R.string.new_movie),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            if (uiState.isFetching) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                }
            } else {
                OutlinedTextField(
                    value = uiState.title ?: "",
                    onValueChange = { editorViewModel.updateTitle(it) },
                    label = { Text(stringResource(R.string.title)) },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                    ),
                    isError = !uiState.validationState.isTitleValid,
                    modifier = Modifier
                        .fillMaxWidth(),
                )

                OutlinedTextField(
                    value = uiState.genre ?: "",
                    onValueChange = { editorViewModel.updateGenre(it) },
                    label = { Text(stringResource(R.string.genre)) },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                    ),
                    isError = !uiState.validationState.isGenreValid,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                OutlinedTextField(
                    value = DateFormat.getDateInstance().format(Date(uiState.date ?: 0)),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.date)) },
                    trailingIcon = {
                        IconButton(
                            onClick = { showDateDialog.value = true },
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )

                OutlinedTextField(
                    value = if (uiState.score != null) uiState.score.toString() else "",
                    onValueChange = { if (it.length < 3) editorViewModel.updateScore(it) },
                    label = { Text(stringResource(R.string.score)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                    isError = !uiState.validationState.isScoreValid,
                    modifier = Modifier
                        .fillMaxWidth()
                )


                if (showDateDialog.value) {
                    DatePickerModal(
                        onDateSelected = { selectedDate ->
                            editorViewModel.updateDate(selectedDate)
                            showDateDialog.value = false
                        },
                        onDismiss = {
                            showDateDialog.value = false
                        }
                    )
                }

                Button(
                    onClick = {
                        if (isEditing) {
                            editorViewModel.updateMovie()
                        } else {
                            editorViewModel.addMovie()
                        }
                    },
                    enabled = uiState.validationState.isValid(),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(if (isEditing) stringResource(R.string.edit_movie) else stringResource(R.string.save_movie))
                }

                if (isEditing) {
                    Button(
                        onClick = {
                            showConfirmModal.value = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(stringResource(R.string.delete_movie))
                    }
                }

                if (showConfirmModal.value) {
                    ConfirmDeletionModal(
                        movieTitle = uiState.title ?: stringResource(R.string.unknown),
                        onConfirm = { editorViewModel.deleteMovie() },
                        onDismiss = { showConfirmModal.value = false }
                    )
                }
            }
        }
    }
}