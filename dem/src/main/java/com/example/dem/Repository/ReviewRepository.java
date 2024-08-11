package com.example.dem.Repository;

import com.example.dem.Model.Movie;
import com.example.dem.Model.Review;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, ObjectId> {
    List<Review> findByIdIn(List<ObjectId> reviewIds);

    List<Review> findByMovie(Movie movie);
}