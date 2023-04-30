package com.sergiodev.movieapp.repository

import com.sergiodev.movieapp.data.model.MovieList

interface MovieRepository {
   suspend fun getUpcomingMovies(): MovieList
   suspend fun getTopRatedMovies(): MovieList
   suspend fun getPopularMovies(): MovieList

}