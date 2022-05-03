/*Created by: Aino Räkköläinen
 * Date: 3.5.2022 */
package com.example.olio_ht;
//This class is used to make Show-objects
public class Show {
    private String theatreID;
    private String theatre;
    private String title;
    private String genres;

    public Show(String theatreID, String movieTheatre, String title, String genres) {
        this.theatreID = theatreID;
        this.theatre = movieTheatre;
        this.title = title;
        this.genres = genres;
    }
    public String getTitle() {
        return this.title;
    }
    public String getGenre() {
        return this.genres;
    }
    public String getTheatreID() {
        return this.theatreID;
    }

    public String getTheatre() {
        return this.theatre;
    }
}
