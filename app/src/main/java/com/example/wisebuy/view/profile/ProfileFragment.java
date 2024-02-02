// ProfileFragment.java
package com.example.wisebuy.view.profile;

import android.content.Intent;
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

import com.example.wisebuy.R;
import com.example.wisebuy.view.auth.Login;
import com.example.wisebuy.viewModels.ProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileFragment extends Fragment {

    private TextView nameText;
    private Button loginButton;
    private ConstraintLayout profilelayout;
    private ConstraintLayout signinlayout;
    private ProfileViewModel profileViewModel;
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



        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getLoginStatus().observe(getViewLifecycleOwner(), userDetails -> {
            signinlayout.setVisibility(View.GONE);
            if (userDetails != null) {
                profilelayout.setVisibility(View.VISIBLE);
                nameText.setText(String.format(userDetails.get(0)));
            } else {
                signinlayout.setVisibility(View.VISIBLE);
                profilelayout.setVisibility(View.GONE);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Login.class);
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
}
