package hu.szandras.cmovietracker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.szandras.cmovietracker.navigation.Screen
import hu.szandras.cmovietracker.screen.MovieDetailScreen
import hu.szandras.cmovietracker.screen.MovieEditorScreen
import hu.szandras.cmovietracker.screen.MovieListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieApp() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = {
                    Text(stringResource(R.string.app_name))
                }
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.MovieList.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            composable(Screen.MovieList.route) {
                MovieListScreen(navController = navController)
            }

            composable(Screen.MovieDetail.route) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull() ?: -1
                MovieDetailScreen(navController = navController, movieId = movieId)
            }

            composable(Screen.MovieEditor.route) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
                MovieEditorScreen(
                    navController = navController,
                    movieId = movieId,
                )
            }
        }
    }
}