/* Created by: Meri Piirainen
 * Date: 3.5.2022
 */
package com.example.olio_ht;
import java.util.Date;

public class Review {
    private String movieTitle;
    //public static String movieName;
    private String username;
    private String date;
    private int stars;
    private String reviewText;
    public Review(String movieTitle, String username, String date, int stars, String reviewText){
        this.movieTitle = movieTitle;
        this.username=username;
        this.date = date;
        this.stars = stars;
        this.reviewText = reviewText;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setStars(int stars) {
        this.stars = stars;
    }
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
    public int getStars() {
        return this.stars;
    }
    public String getDate() {
        return this.date;
    }
    public String getReviewText() {
        return this.reviewText;
    }
    public String getMovieTitle() {
        return this.movieTitle;
    }
    public String getUsername() { return this.username;}
}

