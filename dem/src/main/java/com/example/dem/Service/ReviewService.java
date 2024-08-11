package com.example.dem.Service;

import com.example.dem.Model.Movie;
import com.example.dem.Model.Review;
import com.example.dem.Repository.MovieRepository;
import com.example.dem.Repository.ReviewRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Review createReview(String reviewBody, String imdbId) {
        Movie movie = movieRepository.findMovieByImdbId(imdbId);
        if (movie == null) {
            throw new RuntimeException("Movie not found with imdbId: " + imdbId);
        }

        Review review = new Review();
        review.setCreated(LocalDateTime.now());
        review.setReviewBody(reviewBody);
        review.setUpdated(LocalDateTime.now());
        review.setImdbId(imdbId);
        review.setMovie(movie);

        Review savedReview = reviewRepository.save(review);

        mongoTemplate.update(Movie.class)
                .matching(Query.query(Criteria.where("imdbId").is(imdbId)))
                .apply(new Update().push("reviewIds", savedReview.getId()))
                .first();

        return savedReview;
    }

    public List<String> getReviewsByMovieImdbId(String imdbId) {
        List<String> reviews=new ArrayList<>();
        Movie movie = movieRepository.findMovieByImdbId(imdbId);
        if (movie == null) {
            throw new RuntimeException("Movie not found with imdbId: " + imdbId);
        }
        for(Review revi:reviewRepository.findByMovie(movie)){
            reviews.add(revi.getReviewBody());
        }
        return reviews;

    }

    public Review updateReview(String imdbId, ObjectId reviewId, String updatedReviewBody) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isEmpty()) {
            throw new RuntimeException("Review not found with id: " + reviewId);
        }

        Review review = optionalReview.get();
        review.setReviewBody(updatedReviewBody);
        review.setUpdated(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    public boolean deleteReview(String imdbId, ObjectId reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isEmpty()) {
            return false;
        }

        Review review = optionalReview.get();
        if (!review.getImdbId().equals(imdbId)) {
            throw new RuntimeException("Review with id " + reviewId + " does not belong to movie with imdbId " + imdbId);
        }

        reviewRepository.delete(review);
        mongoTemplate.update(Movie.class)
                .matching(Query.query(Criteria.where("imdbId").is(imdbId)))
                .apply(new Update().pull("reviewIds", reviewId))
                .first();

        return true;
    }
}
