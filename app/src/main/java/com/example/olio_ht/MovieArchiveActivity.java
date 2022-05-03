/*Created by: Aino Räkköläinen
 * Date: 3.5.2022
 * Sources: */
package com.example.olio_ht;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MovieArchiveActivity extends AppCompatActivity {
    private ArrayList<String> movieTitles = new ArrayList<>();
    private String filename="moviearchive.txt";
    Context context = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_archive);
        context = MovieArchiveActivity.this;
        ListView moviesList = (ListView) findViewById(R.id.reviews);
        //Gettting the arrayList of the Movie-objects
        MoviesData allMovies = new MoviesData();
        allMovies.readEventXML("https://www.finnkino.fi/xml/Events/");
        ArrayList<Movie> Movies = allMovies.getMovies();
        //Writing and reading the archives
        writeArchives(Movies, filename, context);
        movieTitles = readArchivesAndSaveToList(filename, context, movieTitles);
        //Setting the list view
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, movieTitles);
        moviesList.setAdapter(arrayAdapter);
    }
    //This method writes the archive file where are the movieTitles
    public void writeArchives(ArrayList<Movie> Movies, String filename, Context context) {
        try {
            OutputStreamWriter myWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            for (int i=0; i < Movies.size(); i++) {
                myWriter.write(Movies.get(i).getTitle() + "\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    //This reads the archive files and then those are saved to arraylist that is displayed in listview
    public ArrayList<String> readArchivesAndSaveToList(String filename, Context context, ArrayList<String> movieTitles) {
        try {
            InputStream ins = context.openFileInput(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));
            String movietitle="";
            while ((movietitle = br.readLine()) != null) {
                movieTitles.add(movietitle);
            }
            ins.close();
            br.close();
        } catch (IOException e) {
            Log.e("IOException", "Virhe syötteessä");
        } finally {
            System.out.println("File read successfully");
        }
        return movieTitles;
    }

}