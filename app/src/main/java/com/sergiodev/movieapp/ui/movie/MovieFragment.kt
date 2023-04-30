package com.sergiodev.movieapp.ui.movie

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.sergiodev.movieapp.R
import com.sergiodev.movieapp.core.Resource
import com.sergiodev.movieapp.data.model.Movie
import com.sergiodev.movieapp.data.remote.MovieDataSource
import com.sergiodev.movieapp.databinding.FragmentMovieBinding
import com.sergiodev.movieapp.presentation.MovieViewModel
import com.sergiodev.movieapp.presentation.MovieViewModelFactory
import com.sergiodev.movieapp.repository.MovieRepositoryImpl
import com.sergiodev.movieapp.repository.RetrofitClient
import com.sergiodev.movieapp.ui.movie.adapters.concat.MovieAdapter
import com.sergiodev.movieapp.ui.movie.adapters.concat.PopularConcatAdapter
import com.sergiodev.movieapp.ui.movie.adapters.concat.TopRatedConcatAdapter
import com.sergiodev.movieapp.ui.movie.adapters.concat.UpcomingConcatAdapter


class MovieFragment : Fragment(R.layout.fragment_movie), MovieAdapter.OnMovieClickListener {

    private lateinit var binding:FragmentMovieBinding
    private val viewModel by viewModels<MovieViewModel> { MovieViewModelFactory(MovieRepositoryImpl(
        MovieDataSource(RetrofitClient.webservice)
    )) }
    private lateinit var concatAdapter: ConcatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieBinding.bind(view)

        concatAdapter = ConcatAdapter()

        viewModel.fetchMainScreenMovies().observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Resource.Loading -> {
                    Log.d("LiveData", "Loading...")
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    concatAdapter.apply {
                        addAdapter(0, UpcomingConcatAdapter(MovieAdapter(result.data.first.results, this@MovieFragment)))
                        addAdapter(1, TopRatedConcatAdapter(MovieAdapter(result.data.second.results, this@MovieFragment)))
                        addAdapter(2, PopularConcatAdapter(MovieAdapter(result.data.third.results, this@MovieFragment)))
                    }
                    binding.rvMovies.adapter = concatAdapter
                }
                is Resource.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d("Error", "${result.exception}")
                }
            }
        })

    }

    override fun onMovieClick(movie: Movie) {
        val action = MovieFragmentDirections.actionMovieFragmentToMovieDetailFragment(movie.poster_path,
            movie.backdrop_path,
            movie.vote_average.toFloat(),
            movie.vote_count,
            movie.overview,
            movie.title,
            movie.release_date,
            movie.original_language,
        )
        findNavController().navigate(action)
    }

}