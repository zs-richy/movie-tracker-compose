package hu.szandras.cmovietracker.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import hu.szandras.cmovietracker.R
import hu.szandras.cmovietracker.entity.MovieEntity
import hu.szandras.cmovietracker.viewmodel.DetailViewModel
import java.text.DateFormat
import java.util.Date

@Composable
fun MovieDetailScreen(
    navController: NavController,
    movieId: Int,
) {
    val detailViewModel: DetailViewModel = hiltViewModel()
    val uiState by detailViewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {
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
            Text(
                text = uiState.movie?.title ?: stringResource(R.string.unknown),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Text(
                text = "${stringResource(R.string.genre)}: ${uiState.movie?.genre ?: stringResource(R.string.unknown)}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Text(
                text = "${stringResource(R.string.date)}: ${
                    uiState.movie?.let {
                        DateFormat.getDateInstance().format(Date(it.date)) ?: stringResource(R.string.unknown)
                    }
                }",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Text(
                text = "${stringResource(R.string.score)}: ${uiState.movie?.score}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }
    }
}
