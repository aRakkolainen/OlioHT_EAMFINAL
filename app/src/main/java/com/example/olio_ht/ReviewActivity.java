/* Created by: Meri Piirainen
 * Date: 3.5.2022
 * Sources:
 * Help for saving the rating bar stars https://www.youtube.com/watch?v=GQsibTm-aw4
 * Help for current date https://www.youtube.com/watch?v=Le47R9H3qow
 */

package com.example.olio_ht;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ReviewActivity extends AppCompatActivity {
    Context context = null;
    Fragment HomeFragment;
    Button button;
    RatingBar ratingStars;
    EditText editText;
    int stars = 0;
    String reviewText="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        context = ReviewActivity.this;


    }
    //This method is used when in homeview the user chooses the movie they want to review so then this opens the
    // reviewing activity and then the review user gave is saved as Review-object. Then the review-object is written in file
    // then it is later used to show in user's own reviews page
  public void onStart() {
      super.onStart();
      try {
          String movieTitle = getIntent().getExtras().get("movieTitle").toString();

          String userName = getIntent().getExtras().get("username").toString();

          TextView title = findViewById(R.id.movieName);
          title.setText(movieTitle);

          TextView Username = findViewById(R.id.userName);
          Username.setText(userName);

          button = findViewById(R.id.buttonSaveReview);
          ratingStars = findViewById(R.id.ratingBar);
          editText = findViewById(R.id.editTextReview);
          //getting the date
          Calendar calendar = Calendar.getInstance();
          String currentDate =
                  DateFormat.getDateInstance().format(calendar.getTime());
          TextView textView = findViewById(R.id.textViewDate);
          textView.setText(currentDate);
          //Catching the star rating into variable
          ratingStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
              @Override
              public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                  stars = (int) ratingBar.getRating();
              }
          });
          //Catching the review text
          editText.setOnKeyListener(new View.OnKeyListener() {
              @Override
              public boolean onKey(View view, int i, KeyEvent keyEvent) {
                  reviewText = editText.getText().toString();
                  // only allowing two lines for the review
                  int maxLines = 2;
                  int lines = editText.getLineCount();
                  if (lines > maxLines) {
                      int start = editText.getSelectionStart();
                      int end = editText.getSelectionEnd();
                      if (start == end && start < reviewText.length() && start > 1) {
                          reviewText = reviewText.substring(0, start - 1) +
                                  reviewText.substring(start);
                      } else {
                          reviewText = reviewText.substring(0, reviewText.length() - 1);
                      }
                      editText.setText(reviewText);
                      editText.setSelection(editText.getText().length());
                  }
                  return true;
              }
          });
          //List of reviews
          ArrayList<Review> reviewList = new ArrayList<>();
          //Saving the results on button click
          button.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Review review = new Review(movieTitle, userName, currentDate, stars,
                          reviewText);
                    /*//Review.movieName = movieTitle
                    Review.setDate(currentDate);
                    Review.setRe(myRating);
                    Review.reviewText = reviewText;*/
                  reviewList.add(review);
                  //adding review to file
                  try {
                      OutputStreamWriter myWriter = new OutputStreamWriter(context.openFileOutput(userName + ".txt", Context.MODE_APPEND));
                      //FileWriter myWriter = new FileWriter("reviewsFile.txt");
                      for (int i = 0 ;i < reviewList.size();i++) {
                          myWriter.write(reviewList.get(i).getUsername() + ";" + reviewList.get(i).getDate()
                                  + ";" + reviewList.get(i).getMovieTitle()+ ";" + (reviewList.get(i).getStars()) + ";" + reviewList.get(i).getReviewText()+ "\n");
                      }
                      myWriter.close();
                      System.out.println("Successfully wrote to the file.");
                  } catch (IOException e) {
                      System.out.println("An error occurred.");
                      e.printStackTrace();
                  }

                  Toast.makeText(ReviewActivity.this,
                          "Arvostelu tallennettu",
                          Toast.LENGTH_SHORT).show();
              }
          });
      } catch(NullPointerException e) {

      }
      Button back = findViewById(R.id.button4);
      View.OnClickListener listener = new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(getBaseContext(), MainActivity.class);
              startActivity(intent);

          }
      };
      back.setOnClickListener(listener);
  }
}