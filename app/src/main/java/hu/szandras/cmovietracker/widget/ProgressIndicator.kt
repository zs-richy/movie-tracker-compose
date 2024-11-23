package hu.szandras.cmovietracker.widget

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable

@Composable
fun LoadingIndicator(isLoading: Boolean) {
    if (isLoading) {
        CircularProgressIndicator()
    }
}