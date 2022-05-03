/*Created by: Aino Räkköläinen
 * Date: 3.5.2022 */
package com.example.olio_ht.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.olio_ht.LogIn;
import com.example.olio_ht.R;
import com.example.olio_ht.databinding.FragmentProfileBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    Context context = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = getContext().getApplicationContext();

        // Logs the user out
        Button signOut = (Button) root.findViewById(R.id.button_logOut);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut(view);
            }
        });
        String fileName = "Username.txt";
        String Username = ReadUsername(fileName);
        TextView username = root.findViewById(R.id.username);
        username.setText(Username);
        return root;
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
            Log.e("IOException", "Virhe syötteessä");
        } finally {
            System.out.println("File read successfully");
        }
        return readUsername;
    }

    //This method signs the user out
    public void signOut(View v) {
        Intent intent = new Intent(getActivity(), LogIn.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}