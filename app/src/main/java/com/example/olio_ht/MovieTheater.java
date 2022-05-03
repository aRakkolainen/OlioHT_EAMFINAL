/*Created by: Aino Räkköläinen
 * Date: 3.5.2022
 * Sources: Sources are only lecture materials in this class
 * //This is class which can be used to make instances of the movietheatres.
 * */

package com.example.olio_ht;


public class MovieTheater {
    private String ID;
    private String name;
    public MovieTheater(String id, String n) {
        this.ID=id;
        this.name=n;
    }
    public String getID() {
        return this.ID;
    }
    public String getName() {
        return this.name;

    }
    public void setID(String id) {
        this.ID = id;

    }

}

