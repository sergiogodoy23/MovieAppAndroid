package com.sergiodev.movieapp.data.remote

import com.sergiodev.movieapp.application.AppConstants
import com.sergiodev.movieapp.data.model.MovieList
import com.sergiodev.movieapp.repository.WebService

class MovieDataSource(private val webService: WebService) {

    suspend fun getUpcomingMovies(): MovieList = webService.getUpcomingMovies(AppConstants.API_KEY)

    suspend fun getTopRatedMovies(): MovieList = webService.getTopRatedMovies(AppConstants.API_KEY)

    suspend fun getPopularMovies(): MovieList = webService.getPopularMovies(AppConstants.API_KEY)
}