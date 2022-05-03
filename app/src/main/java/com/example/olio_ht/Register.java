/* Created by: Emma Niemenmaa
 * Date: 3.5.2022
 */

package com.example.olio_ht;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    private EditText register_password, register_username, register_email;
    private Button register_button;
    final String fileName = "UserData.txt";
    private String username, password, email;

    ArrayList userList;
    Context context = null;
    Map<String, String> userMap;
    User user;

    public Register() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_username = findViewById(R.id.inputText_username);
        register_password = findViewById(R.id.inputText_password);
        register_email = findViewById(R.id.inputText_email);
        register_button = findViewById(R.id.button_registerActivity);
        userList = new ArrayList<>();
        userMap = new HashMap<>();
        user = new User(username, password, email);


        // Defines the location of the application (for the application)
        context = Register.this;

        userMap = ReadFileIntoMap(fileName);

        // Moving back to login activity after successfully creating an account
        register_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (CheckUserInput(register_username.getText().toString(), register_password.getText().toString(), userMap)) {
                    writeUserData(fileName);
                    openActivityMainActivity();
                }
            }
        });
    }

    // Method to start LogIn activity
    public void openActivityMainActivity() {
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }



    // Method to see if the username is already taken
    private boolean CheckUserInput(String Username, String Password, Map<String, String> mapUsers) {
        // Search numbers from password
        char[] charPassword = Password.toCharArray();
        boolean isNumber = false;
        for (char c : charPassword) {
            if (Character.isDigit(c)) {
                isNumber = true;
                break;
            }
        }

        // Search upper case letters from password
        boolean containsUpperCaseLetter = false;
        for(int i=0;i<Password.length();i++){
            if(Character.isUpperCase(Password.charAt(i))){
                containsUpperCaseLetter = true;
            }
        }
        // Search lower case letters from password
        boolean containsLowerCaseLetter = false;
        for(int i=0;i<Password.length();i++){
            if(Character.isLowerCase(Password.charAt(i))){
                containsLowerCaseLetter = true;
            }
        }

        // Create variable that has true value if the Password contains a special character
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(Password);
        boolean stringContainsSpecialCharacter = matcher.find();

        // Checks other conditions (no empty submits, no duplicate usernames)
        if (mapUsers.containsKey(Username)) {
            register_username.setError("Käyttäjänimi on jo varattu!");
        } else if (Username.equals("")) {
            register_username.setError("Anna käyttäjänimi!");
        } else if (Password.equals("")) {
            register_password.setError("Anna salasana!");
        } else if (!isNumber) {
            register_password.setError("Salasanassa pitää olla numeroita.");
        } else if (Password.length() < 12){
            register_password.setError("Salasana on liian lyhyt. Minimi mitta salasanalle on 12 merkkiä.");
        } else if (!stringContainsSpecialCharacter) {
            register_password.setError("Salasanan tulee sisältää vähintään yksi erikoismerkki.");
        } else if (!containsLowerCaseLetter || !containsUpperCaseLetter) {
            register_password.setError("Salasanassa tulee olla vähintään yksi iso ja yksi pieni kirjain.");
        } else {
            // if the info given by the user is valid, return true
            AddUser();
            return true;
        }
        return false;
    }


    // Method to add new users data to an array list
    public void AddUser() {
        user = new User(username, password, email);
        String Username = register_username.getText().toString();
        String Password = register_password.getText().toString();
        String Email = register_email.getText().toString();

        user.setUsername(Username);
        user.setPassword(Password);
        user.setEmail(Email);
    }


    // Method to read old userData into a hashmap
    public Map<String, String> ReadFileIntoMap(String fileName) {
        try {
            InputStream ins = context.openFileInput(fileName);

            BufferedReader br = new BufferedReader(new InputStreamReader(ins));

            String s = "";

            // Goes through every line in the file
            while ((s = br.readLine()) != null) {
                // split the row by every ":"
                String[] row = s.split(":");

                // first part is the username, second is the number (and third would be email)
                String name = row[0];
                String number = row[1];

                // put name, number in HashMap if they are not empty:
                if (!name.equals("") && !number.equals(""))
                    userMap.put(name, number);
            }
            // Close the InputStream
            ins.close();
        } catch (IOException e) {
            Log.e("IOException", "Virhe syötteessä");
        }
        return userMap;
    }



    // Method to write contents of arraylist to UserData -file
    public void writeUserData(String fileName) {
        try {
            OutputStreamWriter ows = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_APPEND));

            String s = "";

            s = (user.getUsername() +":"+ user.getPassword() +":"+ user.getEmail() +"\n");
            ows.append(s);
            ows.close();

        } catch (IOException e) {
            Log.e("IOException", "Virhe syötteessä");
        }
    }
}