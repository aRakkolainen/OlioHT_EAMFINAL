/*Created by: Aino Räkköläinen
 * Date: 3.5.2022 */
package com.example.olio_ht;

public class Movie{
    private String title;
    private String genres;
    Movie(String title, String genre) {
        this.title = title;
        this.genres = genre;
    }
    public String getTitle() {
        return this.title;
    }
    public String getGenre() {
        return this.genres;
    }
}
