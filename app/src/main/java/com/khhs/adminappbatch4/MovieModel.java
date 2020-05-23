package com.khhs.adminappbatch4;

public class MovieModel {

    public  String movieTitle;
    public String movieImage;
    public String movieVideo;
    public String movieCategory;

    public String createdAt;

    public int movieCount;

    public MovieModel(String movieTitle, String movieImage, String movieVideo, String movieCategory, String createdAt, int movieCount) {
        this.movieTitle = movieTitle;
        this.movieImage = movieImage;
        this.movieVideo = movieVideo;
        this.movieCategory = movieCategory;
        this.createdAt = createdAt;
        this.movieCount = movieCount;
    }

    public MovieModel(String movieTitle, String movieImage, String movieVideo, String movieCategory, String createdAt) {
        this.movieTitle = movieTitle;
        this.movieImage = movieImage;
        this.movieVideo = movieVideo;
        this.movieCategory = movieCategory;
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public MovieModel(String movieTitle, String movieImage, String movieVideo, String movieCategory) {
        this.movieTitle = movieTitle;
        this.movieImage = movieImage;
        this.movieVideo = movieVideo;
        this.movieCategory = movieCategory;
    }

    public MovieModel() {
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }

    public String getMovieVideo() {
        return movieVideo;
    }

    public void setMovieVideo(String movieVideo) {
        this.movieVideo = movieVideo;
    }

    public String getMovieCategory() {
        return movieCategory;
    }

    public void setMovieCategory(String movieCategory) {
        this.movieCategory = movieCategory;
    }
}
