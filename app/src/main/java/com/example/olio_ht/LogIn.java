/* Created by: Emma Niemenmaa
 * Date: 3.5.2022
 * Sources used in LogIn and Register:
 * - Error messages
 * https://handyopinion.com/login-activity-in-android-studio-kotlin-java/
 * - Button to move between activities
 * https://www.youtube.com/watch?v=bgIUdb-7Rqo
 * - Using a hashmap
 * https://www.youtube.com/watch?v=3iv8WlVKo0I
 * - Methods containsValue and containsKey
 * https://www.geeksforgeeks.org/hashmap-containsvalue-method-in-java/
 * https://www.geeksforgeeks.org/hashmap-containskey-method-in-java/
 * - Reading file into a hashmap
 * https://www.geeksforgeeks.org/reading-text-file-into-java-hashmap/
 * - Writing to a file
 * https://www.youtube.com/watch?v=wGH-D4jRS4k
 * - Check if string contains uppercase / lowercase letters
 * https://www.codegrepper.com/code-examples/java/java+check+string+contains+uppercase+character
 * - Check if string contains special characters
 * https://javahungry.blogspot.com/2020/06/check-string-contains-special-characters.html
 */

package com.example.olio_ht;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class LogIn extends AppCompatActivity {

    private EditText password, username;
    private Button login;
    final String fileName = "UserData.txt";
    final String fileNameUser = "Username.txt";
    //Creating a hashmap to store usernames and passwords
    Map<String, String> userMap;
    Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // initialisations
        password = findViewById(R.id.inputText_password);
        username = findViewById(R.id.inputText_username);
        Button forgotPassword = findViewById(R.id.button_ForgotPassword);
        login = findViewById(R.id.button_login);
        Button register = findViewById(R.id.button_Register);
        userMap = new HashMap<>();

        context = LogIn.this;

        userMap = ReadFileIntoMap(fileName);

        // Method to take the user to forgot password -activity
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityForgotPassword();
            }
        });

        // Method to take the user to login activity
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPasswordAndUsername(password.getText().toString(), username.getText().toString(), userMap)) {
                    login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            writeUsername(fileNameUser);
                            openActivityMainActivity();
                        }
                    });
                }
            }
        });

        // Button for register and method to take the user to register activity
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityRegister();
            }
        });

    }

    // Method to move from LogIn activity to Home fragment
    public void openActivityMainActivity() {
        String Username = username.getText().toString();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", Username);
        startActivity(intent);
    }

    // Method to start register activity
    public void openActivityRegister() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    // Method to start forgot password -activity
    public void openActivityForgotPassword() {
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }

    // Checks if the user has given username and password correctly:
    private boolean checkPasswordAndUsername(String Password, String Username, Map<String, String> mapUsers) {
        // No username given
        if (Username.equals("")) {
            username.setError("Anna käyttäjätunnus");

        // No password given
        } if (Password.equals("")) {
            this.password.setError("Anna salasana");

        // Correct password and username
        } if ((userMap.containsKey(Username)) && (userMap.get(Username).equals(Password))) {
            return true;

        // Wrong password or username
        } else {
            password.setError("Virheellinen salasana tai käyttäjätunnus.");
        }
        return false;
    }

    // Overwrites "User.txt" file to pass the username to HomeFragment
    public void writeUsername(String fileNameUser) {
        try {
            OutputStreamWriter ows2 = new OutputStreamWriter(context.openFileOutput(fileNameUser, Context.MODE_PRIVATE));

            String Username = username.getText().toString();
            ows2.write(Username);
            ows2.close();

        } catch (IOException e) {
            Log.e("IOException", "Virhe syötteessä");
        } finally {
            System.out.println("File written successfully");
        }
    }


    // Method to read contents of UserData -file to a hashmap
    public Map<String, String> ReadFileIntoMap(String fileName) {

        try {
            InputStream ins = context.openFileInput(fileName);

            BufferedReader br = new BufferedReader(new InputStreamReader(ins));

            String s = "";

            while ((s = br.readLine()) != null) {

                // split the row by every ":"
                String[] row = s.split(":");

                // first part is name, second is number
                String name = row[0].trim();
                String number = row[1].trim();

                // put name, number in HashMap if they are not empty:
                if (!name.equals("") && !number.equals(""))
                    userMap.put(name, number);
            }

            ins.close();
            br.close();
        } catch (IOException e) {
            Log.e("IOException", "Virhe syötteessä");
        } finally {
            System.out.println("LUETTU");
        }
        return userMap;
    }
}