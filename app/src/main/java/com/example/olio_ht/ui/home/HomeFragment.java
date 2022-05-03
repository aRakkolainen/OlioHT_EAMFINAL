/*Created by: Aino Räkköläinen
 * Date: 3.5.2022
 * Sources:
 * How to use spinner component:
 * https://stackoverflow.com/questions/42253936/spinner-setonitemselectedlistener-in-fragment
 * Android - Spinner (tutorialspoint.com).
 * How to use the DatePicker component:
 * https://www.tutorialspoint.com/android/android_datepicker_control.htm
 * https://abhiandroid.com/ui/datepicker
 * Usage of ListView component for showing list of movie titles:
 * https://stackoverflow.com/questions/5070830/populating-a-listview-using-an-arraylist
 * How to use arrayLists:
 * https://www.w3schools.com/java/java_arraylist.asp
 * */

package com.example.olio_ht.ui.home;

import static android.widget.AdapterView.*;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.olio_ht.LogIn;
import com.example.olio_ht.ReviewActivity;
import com.example.olio_ht.Movie;
import com.example.olio_ht.MovieArchiveActivity;
import com.example.olio_ht.MovieTheater;
import com.example.olio_ht.MovieTheaterData;
import com.example.olio_ht.MoviesData;
import com.example.olio_ht.R;
import com.example.olio_ht.Show;
import com.example.olio_ht.ShowData;
import com.example.olio_ht.databinding.FragmentHomeBinding;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class HomeFragment extends Fragment {
    private String urlString="https://www.finnkino.fi/xml/TheatreAreas/";
    private String urlString2="https://www.finnkino.fi/xml/Schedule/?area=";
    private String url;
    private String scheduleUrl="https://www.finnkino.fi/xml/Schedule/";
    private String theId;
    private String Username;
    private String fileName = "Username.txt";
    private Spinner spinner;
    private String date="";
    private TextView movieText;
    private HashMap<String, String> movieTheatres = new HashMap<String, String>();
    View v;
    private ArrayList<String> movieTheaterNames = new ArrayList<String>();
    private ArrayList<String> movieTitles = new ArrayList<String>();
    private ArrayList<Show> Shows = new ArrayList<Show>();
    private ArrayList<Movie> Movies = new ArrayList<Movie>();
    ArrayList<String> titles = new ArrayList<String>();
    Context context = null;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Logs the user out
        Button signOut = (Button) root.findViewById(R.id.signOut);
        signOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut(view);
            }
        });

        // Defines the file path
        context = getContext().getApplicationContext();

        Username = ReadUsername(fileName);

        // Defining the spinner and movieArchive button
        spinner = root.findViewById(R.id.spinner2);
        Button movieAchive = (Button) root.findViewById(R.id.button2);
        //Making list of movieTheaters as MovieTheater-objects
        ArrayList<MovieTheater> movieTheaters = readXML(urlString);

        // Making list of names of movieTheaters and hashMap with the ids:
        for (int i = 0; i < movieTheaters.size(); i++) {
            movieTheaterNames.add(movieTheaters.get(i).getName());
            movieTheatres.put(movieTheaters.get(i).getName(), movieTheaters.get(i).getID());
        }

        // Making list for all Movies based on Events XML in Finnkino API
        MoviesData allMovies = new MoviesData();
        allMovies.readEventXML("https://www.finnkino.fi/xml/Events/");
        Movies = allMovies.getMovies();
        for (int j = 0; j < Movies.size(); j++) {
            movieTitles.add(Movies.get(j).getTitle());
        }
        // Making list for all shows based on Schedule XML of Finnkino is done in each case
        //Using the datePicker-component to get the date from user:
        DatePicker simpleDatePicker = (DatePicker) root.findViewById(R.id.datePicker);
        simpleDatePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int yearNumber, int monthNumber, int dayNumber) {
                Date date = new Date(dayNumber, monthNumber, yearNumber);
                 String day= String.valueOf(dayNumber);
                 String month = String.valueOf(monthNumber+1);
                 String year = String.valueOf(yearNumber);
            }

        });
        Date currentDate = new Date(simpleDatePicker.getDayOfMonth(), simpleDatePicker.getMonth(), simpleDatePicker.getYear());
        String day= String.valueOf(currentDate.getDay());
        String month = String.valueOf(currentDate.getMonth()+1);
        String year = String.valueOf(currentDate.getYear());
        String date = day+"."+month+"."+year;

        movieText = (TextView) root.findViewById(R.id.textView);
        /*This is the spinner used for listing the movieTheatres and then with method onItemSelected and
        * switch-case structure is defined those movieTheatres the user can choose and which movies then
        * user can see. In each case, there is first set the title text, then there is found the id
        * of that movietheater and then is defined variable url which is used for parsing the Finnkino's
        * XML-data of specific area.
        */

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                String movieTheaterChoice = item.toString();
                if (item != null & date != null) {
                    Toast.makeText(getContext(), item.toString(), Toast.LENGTH_SHORT).show();
                        switch (movieTheaterChoice) {
                            case ("Pääkaupunkiseutu"):
                                Shows.clear();
                                movieText.setText("Elokuvat Pääkaupunkiseutu");
                                theId = movieTheatres.get("Pääkaupunkiseutu");
                                url=urlString2+theId+"&dt=";//+date;
                                ShowData paakaupunkiseutu = new ShowData();
                                paakaupunkiseutu.readScheduleXML(url);
                                Shows = paakaupunkiseutu.getShows();
                                break;
                            case ("Espoo"):
                                Shows.clear();
                                movieText.setText("Elokuvat Espoo");
                                ShowData espoo = new ShowData();
                                espoo.readScheduleXML(url);
                                Shows = espoo.getShows();
                                break;
                            case ("Espoo: OMENA"):
                                Shows.clear();
                                movieText.setText("Elokuvat Espoo OMENA");
                                //String theId =movieTheatres.get("Omena, Espoo");
                                theId = movieTheatres.get("Espoo: OMENA");
                                url=urlString2+theId+"&dt="+date;
                                ShowData espooOmena = new ShowData();
                                espooOmena.readScheduleXML(url);
                                Shows = espooOmena.getShows();
                                break;
                            case ("Espoo: SELLO"):
                                Shows.clear();
                                movieText.setText("Elokuvat Espoo SELLO");
                                theId = movieTheatres.get("Espoo: SELLO");
                                url=urlString2+theId+"&dt=";//+date;
                                ShowData espooSello = new ShowData();
                                espooSello.readScheduleXML(url);
                                Shows = espooSello.getShows();
                                break;
                            case ("Helsinki"):
                                Shows.clear();
                                movieText.setText("Elokuvat Helsinki");
                                break;
                            case ("Helsinki: ITIS"):
                                Shows.clear();
                                movieText.setText("Elokuvat Helsinki ITIS");
                                theId = movieTheatres.get("Helsinki: ITIS");
                                url=urlString2+theId;
                                ShowData helsinkiItis = new ShowData();
                                helsinkiItis.readScheduleXML(url);
                                Shows =helsinkiItis.getShows();
                                break;
                            case ("Helsinki: KINOPALATSI"):
                                Shows.clear();
                                movieText.setText("Elokuvat Helsinki KINOPALATSI");
                                theId = movieTheatres.get("Helsinki: KINOPALATSI");
                                url=urlString2+theId;
                                ShowData helsinkiKinopalatsi = new ShowData();
                                helsinkiKinopalatsi.readScheduleXML(url);
                                Shows =helsinkiKinopalatsi.getShows();
                                break;
                            case ("Helsinki: MAXIM"):
                                Shows.clear();
                                movieText.setText("Elokuvat Helsinki MAXIM");
                                theId = movieTheatres.get("Helsinki: MAXIM");
                                url=urlString2+theId;
                                ShowData helsinkiMaxim = new ShowData();
                                helsinkiMaxim.readScheduleXML(url);
                                Shows =helsinkiMaxim.getShows();
                                break;
                            case ("Helsinki: TENNISPALATSI"):
                                Shows.clear();
                                movieText.setText("Elokuvat Helsinki TENNISPALATSI");
                                theId = movieTheatres.get("Helsinki: TENNISPALATSI");
                                url=urlString2+theId;
                                ShowData helsinkiTennispalatsi = new ShowData();
                                helsinkiTennispalatsi.readScheduleXML(url);
                                Shows =helsinkiTennispalatsi.getShows();
                                break;
                            case ("Vantaa: FLAMINGO"):
                                Shows.clear();
                                movieText.setText("Elokuvat Vantaa FLAMINGO");
                                theId = movieTheatres.get("Vantaa: FLAMINGO");
                                url=urlString2+theId;
                                ShowData vantaaFlamingo = new ShowData();
                                vantaaFlamingo.readScheduleXML(url);
                                Shows =vantaaFlamingo.getShows();
                                break;
                            case ("Jyväskylä: FANTASIA"):
                                Shows.clear();
                                movieText.setText("Elokuvat Jyväskylä FANTASIA");
                                theId = movieTheatres.get("Jyväskylä: FANTASIA");
                                url=urlString2+theId;
                                ShowData jyvaskylaFantasia = new ShowData();
                                jyvaskylaFantasia.readScheduleXML(url);
                                Shows =jyvaskylaFantasia.getShows();
                                break;
                            case ("Kuopio: SCALA"):
                                Shows.clear();
                                movieText.setText("Elokuvat Kuopio SCALA");
                                theId = movieTheatres.get("Kuopio: SCALA");
                                url=urlString2+theId;
                                ShowData kuopioScala = new ShowData();
                                kuopioScala.readScheduleXML(url);
                                Shows =kuopioScala.getShows();
                                break;
                            case ("Lahti: KUVAPALATSI"):
                                Shows.clear();
                                movieText.setText("Elokuvat Lahti: KUVAPALATSI");
                                theId = movieTheatres.get("Lahti: KUVAPALATSI");
                                url=urlString2+theId;
                                ShowData lahtiKuvapalatsi = new ShowData();
                                lahtiKuvapalatsi.readScheduleXML(url);
                                Shows =lahtiKuvapalatsi.getShows();
                                break;
                            case ("Lappeenranta: STRAND"):
                                Shows.clear();
                                movieText.setText("Elokuvat Lappeenranta: STRAND");
                                theId = movieTheatres.get("Lappeenranta: STRAND");
                                url=urlString2+theId;
                                ShowData lappeenrantaStrand = new ShowData();
                                lappeenrantaStrand.readScheduleXML(url);
                                Shows =lappeenrantaStrand.getShows();
                                break;
                            case ("Oulu: PLAZA"):
                                Shows.clear();
                                movieText.setText("Elokuvat Oulu: PLAZA");
                                theId = movieTheatres.get("Oulu: PLAZA");
                                url=urlString2+theId;
                                ShowData ouluPlaza = new ShowData();
                                ouluPlaza.readScheduleXML(url);
                                Shows =ouluPlaza.getShows();
                                break;
                            case ("Pori: PROMENADI"):
                                Shows.clear();
                                movieText.setText("Elokuvat Pori PROMENADI");
                                theId = movieTheatres.get("Pori: PROMENADI");
                                url=urlString2+theId;
                                ShowData poriPromenadi = new ShowData();
                                poriPromenadi.readScheduleXML(url);
                                Shows =poriPromenadi.getShows();
                                break;
                            case ("Tampere"):
                                Shows.clear();
                                movieText.setText("Elokuvat Tampere");
                                theId = movieTheatres.get("Tampere");
                                url=urlString2+theId;
                                ShowData tampere = new ShowData();
                                tampere.readScheduleXML(url);
                                Shows =tampere.getShows();
                                break;
                            case ("Tampere: CINE ATLAS"):
                                Shows.clear();
                                movieText.setText("Elokuvat Tampere CINE ATLAS");
                                theId = movieTheatres.get("Tampere: CINE ATLAS");
                                url=urlString2+theId;
                                ShowData tampereCineAtlas = new ShowData();
                                tampereCineAtlas.readScheduleXML(url);
                                Shows =tampereCineAtlas.getShows();
                                break;
                            case ("Tampere: PLEVNA"):
                                Shows.clear();
                                movieText.setText("Elokuvat Tampere PLEVNA");
                                theId = movieTheatres.get("Tampere: PLEVNA");
                                url=urlString2+theId;
                                ShowData tamperePlevna = new ShowData();
                                tamperePlevna.readScheduleXML(url);
                                Shows =tamperePlevna.getShows();
                                break;
                            case ("Turku: KINOPALATSI"):
                                Shows.clear();
                                movieText.setText("Elokuvat Turku KINOPALATSI");
                                theId = movieTheatres.get("Turku: KINOPALATSI");
                                url=urlString2+theId;
                                ShowData turkuKinopalatsi = new ShowData();
                                turkuKinopalatsi.readScheduleXML(url);
                                Shows =turkuKinopalatsi.getShows();
                                break;
                            case ("Raisio: LUXE MYLLY"):
                                Shows.clear();
                                movieText.setText("Elokuvat Raisio LUKE MYLLY");
                                theId = movieTheatres.get("Raisio: LUKE MYLLY");
                                url=urlString2+theId;
                                ShowData raisioLuxeMylly = new ShowData();
                                raisioLuxeMylly.readScheduleXML(url);
                                Shows =raisioLuxeMylly.getShows();
                            default:
                                Shows.clear();
                                url=scheduleUrl;
                                movieText.setText("Kaikki elokuvat");
                                ShowData allShows = new ShowData();
                                allShows.readScheduleXML(url);
                                Shows = allShows.getShows();

                        }
                        ShowData show = new ShowData();
                        show.readScheduleXML(urlString2);
                        Shows = show.getShows();
                        titles.clear();

                        for (int i=0; i < Shows.size(); i++) {
                            if(titles.contains(Shows.get(i).getTitle())) {
                                continue;
                            } else {
                                titles.add(Shows.get(i).getTitle());
                            }
                        }
                    Collections.sort(titles);
                    // Making the listView show the movieTitles in each cases (This is done based to tutorial website mentioned in documentation)
                    ListView moviesList = (ListView) root.findViewById(R.id.shows);
                    ArrayAdapter arrayAdapter =new ArrayAdapter<String>(getContext().getApplicationContext(), android.R.layout.simple_list_item_1, titles);
                    moviesList.setAdapter(arrayAdapter);
                    moviesList.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            String selectedMovie = titles.get(position);
                            Toast.makeText(getContext().getApplicationContext(), "Movie Selected : " + selectedMovie, Toast.LENGTH_LONG).show();
                            //Sending the movietitle and username to reviewActivity
                            Intent intent = new Intent(getActivity().getBaseContext(), ReviewActivity.class);
                            intent.putExtra("movieTitle", selectedMovie);
                            intent.putExtra("username", Username);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        }); {

        }
        //here is defined the spinner/dropdown menu for movietheaters.
        ArrayAdapter stringArrayAdapter = new ArrayAdapter(getContext().getApplicationContext(), android.R.layout.simple_spinner_item, movieTheaterNames);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(stringArrayAdapter);
        //This is the button for opening the movieArchive
        movieAchive.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openMovieArchiveActivity(view);
            }
        });

        return root;
    }
    //This method opens the MovieArchiveActivity
    public void openMovieArchiveActivity(View v) {
        Intent intent = new Intent(getActivity(), MovieArchiveActivity.class);
        getActivity().startActivity(intent);
    }

    //This method sign the user out
    public void signOut(View v) {
        Intent intent = new Intent(getActivity(), LogIn.class);
        getActivity().startActivity(intent);
    }

    public ArrayList<MovieTheater> readXML(String urlString) {
        ArrayList<MovieTheater> movieTheaters = new ArrayList<MovieTheater>();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(urlString);
            MovieTheaterData movietheaterdata = new MovieTheaterData(doc);
            movieTheaters = movietheaterdata.getMovieTheaters();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return movieTheaters;


    }


    public String ReadUsername(String fileName) {
        String readUsername = "";
        try {
            InputStream ins = context.openFileInput(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));

            readUsername = br.readLine();
            ins.close();
            br.close();
        } catch (IOException e) {
            Log.e("IOException", "Virhe syötteessä");
        } finally {
            System.out.println("Tiedosto luettu");
        }
        return readUsername;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}