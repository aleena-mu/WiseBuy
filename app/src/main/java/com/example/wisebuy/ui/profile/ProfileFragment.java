// ProfileFragment.java
package com.example.wisebuy.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.wisebuy.Login;
import com.example.wisebuy.LoginOtp;
import com.example.wisebuy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private TextView nameText;
    private Button loginButton;
    private ConstraintLayout profilelayout;
    private ConstraintLayout signinlayout;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        nameText = root.findViewById(R.id.nameText);
        signinlayout=root.findViewById(R.id.sigininlayout);
        profilelayout=root.findViewById(R.id.profilelayout);
        loginButton = root.findViewById(R.id.loginButton);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();



        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Check if the user is logged in
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            // User is logged in, perform actions accordingly
           signinlayout.setVisibility(View.GONE);

            String phoneNumber = currentUser.getPhoneNumber();
            showUserDetails(phoneNumber);
            // Now, you can show the profile details or perform other actions
            // based on the fact that the user is logged in.
        } else {
            profilelayout.setVisibility(View.GONE);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),Login.class);

                    startActivity(intent);

                }
            });

        }
    }

        private void showUserDetails(String phoneNumber) {
        // Retrieve user details from Firestore based on the phone number
        firestore.collection("Users")
                .whereEqualTo("Phone_Number", phoneNumber)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String userName = document.getString("User_Name");
                            //String userPlace = document.getString("Place");

                            // Display user details

                            nameText.setText(String.format(userName));


                        }
                    } else {
                        // Handle error
                    }
                });
    }
}
