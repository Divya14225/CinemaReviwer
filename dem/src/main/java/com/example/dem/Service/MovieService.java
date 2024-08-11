package com.example.dem.Service;

import com.example.dem.Model.Movie;
import com.example.dem.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> allMovies() {
        return movieRepository.findAll();
    }

    public Movie singleMovie(String imdbId) {
        return movieRepository.findMovieByImdbId(imdbId);
    }

    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie updateMovie(String imdbId, Movie updatedMovie) {
        Movie existingMovie = movieRepository.findMovieByImdbId(imdbId);
        if (existingMovie == null) {
            throw new RuntimeException("Movie not found with imdbId: " + imdbId);
        }

        existingMovie.setTitle(updatedMovie.getTitle());
        existingMovie.setReleaseDate(updatedMovie.getReleaseDate());
        existingMovie.setTrailerLink(updatedMovie.getTrailerLink());
        existingMovie.setGenres(updatedMovie.getGenres());
        existingMovie.setPoster(updatedMovie.getPoster());
        existingMovie.setBackdrops(updatedMovie.getBackdrops());

        return movieRepository.save(existingMovie);
    }

    public void deleteMovie(String imdbId) {
        Movie existingMovie = movieRepository.findMovieByImdbId(imdbId);
        if (existingMovie == null) {
            throw new RuntimeException("Movie not found with imdbId: " + imdbId);
        }
        movieRepository.delete(existingMovie);
    }
}
