// ProfileFragment.java
package com.example.wisebuy.view.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wisebuy.MainActivity;
import com.example.wisebuy.R;
import com.example.wisebuy.view.auth.Login;
import com.example.wisebuy.viewModels.ProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileFragment extends Fragment {

    private TextView nameText, logoutText;
    private Button loginButton;
    private ConstraintLayout profilelayout;
    private ConstraintLayout signinlayout;
    private ProfileViewModel profileViewModel;
    private final String USERPREFERNCE = "User";

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        nameText = root.findViewById(R.id.nameText);
        signinlayout = root.findViewById(R.id.sigininlayout);
        profilelayout = root.findViewById(R.id.profilelayout);
        loginButton = root.findViewById(R.id.loginButton);
        logoutText = root.findViewById(R.id.logout);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getLoggedInUser().observe(getViewLifecycleOwner(), userDetails -> {
            signinlayout.setVisibility(View.GONE);
            if (userDetails != null) {
                saveUsernameToSharedPreferences(userDetails.getName());
                profilelayout.setVisibility(View.VISIBLE);
                SharedPreferences preferences = requireActivity().getSharedPreferences(USERPREFERNCE, Context.MODE_PRIVATE);
                String userName = preferences.getString("username", null);
                String formattedName = userName != null ? userName : "";
                nameText.setText(formattedName);
            } else {
                signinlayout.setVisibility(View.VISIBLE);
                profilelayout.setVisibility(View.GONE);
            }
        });
        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileViewModel.logout();

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);

            }
        });

//        private void showUserDetails (String phoneNumber){
//            // Retrieve user details from Firestore based on the phone number
//            firestore.collection("Users")
//                    .whereEqualTo("Phone_Number", phoneNumber)
//                    .get()
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            for (DocumentSnapshot document : task.getResult()) {
//                                String userName = document.getString("User_Name");
//                                //String userPlace = document.getString("Place");
//
//                                // Display user details
//
//                                assert userName != null;
//                                nameText.setText(String.format(userName));
//
//
//                            }
//                        } else {
//                            // Handle error
//                        }
//                    });
//        }

    }

    private void saveUsernameToSharedPreferences(String username) {
        SharedPreferences preferences = requireContext().getSharedPreferences(USERPREFERNCE, Context.MODE_PRIVATE);

        // Check if the preference is empty before saving
        if (preferences.getString("username", null) == null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("username", username);
            editor.apply();
        }
    }

}
