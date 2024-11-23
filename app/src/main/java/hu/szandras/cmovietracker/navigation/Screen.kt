package hu.szandras.cmovietracker.navigation

sealed class Screen(val route: String) {
    object MovieList : Screen("movie_list")
    object MovieDetail : Screen("movie_detail/{movieId}") {
        fun createRoute(movieId: Int) = "movie_detail/$movieId"
    }
    object MovieEditor : Screen("movie_editor?movieId={movieId}") {
        fun createRoute(movieId: Int? = null) =
            "movie_editor?movieId=${movieId ?: -1}"
    }
}