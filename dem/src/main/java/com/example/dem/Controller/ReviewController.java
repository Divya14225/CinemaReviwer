package com.example.dem.Controller;

import com.example.dem.Model.Review;
import com.example.dem.Service.ReviewService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // POST endpoint to create a review for a movie by IMDb ID
    @PostMapping("/{imdbId}")
    public ResponseEntity<Review> createReview(@RequestBody String reviewBody, @PathVariable String imdbId) {
        return new ResponseEntity<>(reviewService.createReview(reviewBody, imdbId), HttpStatus.CREATED);
    }

    // PUT endpoint to update a review by IMDb ID and review ID
    @PutMapping("/{imdbId}/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable String imdbId, @PathVariable ObjectId reviewId, @RequestBody String updatedReviewBody) {
        Review updatedReview = reviewService.updateReview(imdbId, reviewId, updatedReviewBody);
        if (updatedReview == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedReview);
    }

    // DELETE endpoint to delete a review by IMDb ID and review ID
    @DeleteMapping("/{imdbId}/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable String imdbId, @PathVariable ObjectId reviewId) {
        if (reviewService.deleteReview(imdbId, reviewId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // GET endpoint to fetch all reviews for a movie by IMDb ID
    @GetMapping("/{imdbId}")
    public ResponseEntity<List<String>> getReviewsByMovieImdbId(@PathVariable String imdbId) {
        List<String> reviews = reviewService.getReviewsByMovieImdbId(imdbId);
        if (reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reviews);
    }
}
