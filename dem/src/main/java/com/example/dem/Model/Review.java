package com.example.dem.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"movie"})
public class Review {
    @Id
    private ObjectId id;
    private String reviewBody;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String imdbId;

    @DBRef
    private Movie movie;

    public Review(String reviewBody, LocalDateTime created, LocalDateTime updated, Movie movie, String imdbId) {
        this.reviewBody = reviewBody;
        this.created = created;
        this.updated = updated;
        this.movie = movie;
        this.imdbId = imdbId;
    }
}
