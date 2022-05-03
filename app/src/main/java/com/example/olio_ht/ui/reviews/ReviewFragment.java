/*Created by: Aino Räkköläinen
 * Date: 3.5.2022
 * Sources:
 * Tutorial how to make custom listView:
 * https://apktutor.com/custom-listview-with-baseadapter-for-displaying-map-list/
 * How to use ratinbar:
 * https://abhiandroid.com/ui/ratingbar
 * */

package com.example.olio_ht.ui.reviews;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.olio_ht.R;
import com.example.olio_ht.Review;
import com.example.olio_ht.databinding.FragmentReviewsBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//This fragment is used to control the user's own reviews page, the order of the reviews
public class ReviewFragment extends Fragment {
    private FragmentReviewsBinding binding;
    Context context = null;
    String reviewText;
    private ListView listview1;
    private ArrayList<HashMap<String, Object>> maplist = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReviewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = getContext().getApplicationContext();

        String fileName = "Username.txt";
        String Username = ReadUsername(fileName);
        ArrayList<Review> reviews = readReviewsFile(Username);
        HashMap<String, ArrayList<Review>> reviewMap= sortReviews(reviews);
        //Setting the values in customized listView
        for (int i=0; i < reviewMap.get("5").size(); i++) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("title", reviewMap.get("5").get(i).getMovieTitle());
            item.put("stars", reviewMap.get("5").get(i).getStars());
            item.put("reviewed", reviewMap.get("5").get(i).getReviewText());
            item.put("date", reviewMap.get("5").get(i).getDate());
            maplist.add(item);
        }
        for (int i=0; i < reviewMap.get("4").size(); i++) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("title", reviewMap.get("4").get(i).getMovieTitle());
            item.put("stars", reviewMap.get("4").get(i).getStars());
            item.put("reviewed", reviewMap.get("4").get(i).getReviewText());
            item.put("date", reviewMap.get("4").get(i).getDate());
            maplist.add(item);
        }
        for (int i=0; i < reviewMap.get("3").size(); i++) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("title", reviewMap.get("3").get(i).getMovieTitle());
            item.put("stars", reviewMap.get("3").get(i).getStars());
            item.put("reviewed", reviewMap.get("3").get(i).getReviewText());
            item.put("date", reviewMap.get("3").get(i).getDate());
            maplist.add(item);
        }
        for (int i=0; i < reviewMap.get("2").size(); i++) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("title", reviewMap.get("2").get(i).getMovieTitle());
            item.put("stars", reviewMap.get("2").get(i).getStars());
            item.put("reviewed", reviewMap.get("2").get(i).getReviewText());
            item.put("date", reviewMap.get("2").get(i).getDate());
            maplist.add(item);
        }
        for (int i=0; i < reviewMap.get("1").size(); i++) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("title", reviewMap.get("1").get(i).getMovieTitle());
            item.put("stars", reviewMap.get("1").get(i).getStars());
            item.put("reviewed", reviewMap.get("1").get(i).getReviewText());
            item.put("date", reviewMap.get("1").get(i).getDate());
            maplist.add(item);
        }
        for (int i=0; i < reviewMap.get("0").size(); i++) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("title", reviewMap.get("0").get(i).getMovieTitle());
            item.put("stars", reviewMap.get("0").get(i).getStars());
            item.put("reviewed", reviewMap.get("0").get(i).getReviewText());
            item.put("date", reviewMap.get("0").get(i).getDate());
            maplist.add(item);
        }
        listview1= (ListView) root.findViewById(R.id.reviews);
        //Defining custom adapter for our map list and displaying it in ListView
        CustomListAdapter adapter =new CustomListAdapter(context, maplist);
        listview1.setAdapter(adapter);
        ((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
        return root;
    }

    //This method reads the data from reviewsFile
    public ArrayList<Review> readReviewsFile(String Username) {
        ArrayList<Review> reviews = new ArrayList<>();
        HashMap<String, ArrayList> userRatings = new HashMap<>();
        //reading the file
        try {
            InputStream ins = context.openFileInput(Username +".txt");

            BufferedReader br = new BufferedReader(new InputStreamReader(ins));
            String s="";
            while ((s=br.readLine()) != null) {
                String[] line = s.split(";");
                String username = line[0];
                String date = line[1];
                String movieTitle = line[2];
                String starsGiven = (line[3]);
                try {
                    reviewText = line[4];
                } catch (ArrayIndexOutOfBoundsException e) {
                    reviewText = "";
                }
                int stars = Integer.valueOf(starsGiven);
                Review review = new Review(movieTitle, username, date, stars, reviewText);
                reviews.add(review);
            }
            ins.close();
            br.close();
        } catch (IOException e){
            Log.e("IOException", "Virhe syötteessä!");

        }
        return reviews;
    }

    //This method sorts the reviews and saves them to hashMap
    public HashMap<String, ArrayList<Review>> sortReviews(@NonNull ArrayList<Review> reviews) {
        HashMap<String, ArrayList<Review>> reviewMap = new HashMap<>();
        ArrayList<Review> zeroStars = new ArrayList<>();
        ArrayList<Review> oneStar = new ArrayList<>();
        ArrayList<Review> twoStars = new ArrayList<>();
        ArrayList<Review> threeStars = new ArrayList<>();
        ArrayList<Review> fourStars = new ArrayList<>();
        ArrayList<Review> fiveStars = new ArrayList<>();
        int stars;
        for (int i=0; i < reviews.size(); i++) {
            stars = reviews.get(i).getStars();
            switch(stars) {
                case (0):
                    zeroStars.add(reviews.get(i));
                    break;
                case (1):
                    oneStar.add(reviews.get(i));
                    break;
                case (2):
                    twoStars.add(reviews.get(i));
                    break;
                case (3):
                    threeStars.add(reviews.get(i));
                    break;
                case (4):
                    fourStars.add(reviews.get(i));
                    break;
                case (5):
                    fiveStars.add(reviews.get(i));
                    break;
            }
        }

        reviewMap.put("0", zeroStars);
        reviewMap.put("1", oneStar);
        reviewMap.put("2", twoStars);
        reviewMap.put("3", threeStars);
        reviewMap.put("4", fourStars);
        reviewMap.put("5", fiveStars);
        return reviewMap;

    }
    //This is needed to display the custom listView and this code is from website:
    // https://apktutor.com/custom-listview-with-baseadapter-for-displaying-map-list/
    public class CustomListAdapter extends BaseAdapter {
        private Context context;
        ArrayList<HashMap<String, Object>> items;
        public CustomListAdapter(Context context, ArrayList<HashMap<String, Object>> items) {
            this.context = context;
            this.items=items;
        }

        @Override
        public int getCount() {
            // This returns total of items in the list
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            //This returns list item at the specified position
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            // Inflate the layout for each list row
            LayoutInflater _inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (v == null) {
                v = _inflater.inflate(R.layout.list_item, null);
            }
            // Set Width of ListView to MATCH_PARENT
            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            // Get the TextView and ImageView from CustomView for displaying item
            TextView txtTitle = (TextView) v.findViewById(R.id.listitemTitle);
            TextView txtStars = (TextView) v.findViewById(R.id.listitemStars);
            RatingBar stars = (RatingBar) v.findViewById(R.id.listitemStarnumber);
            TextView txtReview = (TextView) v.findViewById(R.id.listitemReview);
            TextView txtDate = (TextView) v.findViewById(R.id.listitemDate);

            // Set the text and image for current item using data from map list
            txtTitle.setText(maplist.get(position).get("title").toString());
            txtStars.setText("Tähdet: " + maplist.get(position).get("stars").toString());
            stars.setRating(Float.parseFloat(maplist.get(position).get("stars").toString()));
            txtReview.setText("Arvostelu: " + maplist.get(position).get("reviewed").toString());
            txtDate.setText("Päivämäärä: " + maplist.get(position).get("date").toString());

            // Return the view for the current row
            return v;
        }

    }
    //This method is used to read the username from file
    public String ReadUsername(String fileName) {
        String readUsername = "";
        try {
            InputStream ins = context.openFileInput(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));

            readUsername = br.readLine();
            ins.close();
            br.close();
        } catch (IOException e) {
            Log.e("IOException", "Error in input");
        } finally {
            System.out.println("File read successfully");
        }
        return readUsername;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}