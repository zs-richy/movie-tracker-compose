package hu.szandras.cmovietracker.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import hu.szandras.cmovietracker.component.homescreen.MovieListItem
import hu.szandras.cmovietracker.navigation.Screen
import hu.szandras.cmovietracker.viewmodel.HomeViewModel

@Composable
fun MovieListScreen(
    navController: NavController,
) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    val gridState = rememberLazyGridState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            if (uiState.isLoading) {
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
                if (uiState.movies.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Text(
                            text = stringResource(R.string.empty_content),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        state = gridState
                    )
                    {
                        items(uiState.movies) { movie ->
                            MovieListItem(
                                movie = movie,
                                onEditClick = {
                                    navController.navigate(
                                        Screen.MovieEditor.createRoute(
                                            movie.id
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                                    }
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }

        ExtendedFloatingActionButton(
            text = { Text(text = stringResource(id = R.string.add_movie)) },
            icon = { Icon(Icons.Default.Add, stringResource(id = R.string.add_movie)) },
            onClick = { navController.navigate(Screen.MovieEditor.createRoute()) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            expanded = gridState.lastScrolledBackward ||
                    !gridState.canScrollBackward
        )
    }
}