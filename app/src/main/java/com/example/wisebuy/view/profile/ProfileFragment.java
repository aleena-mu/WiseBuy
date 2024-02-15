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
import com.example.wisebuy.MyPreference;
import com.example.wisebuy.R;
import com.example.wisebuy.view.auth.LoginActivity;
import com.example.wisebuy.viewModels.ProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileFragment extends Fragment {

    private TextView nameText, logoutText;
    private Button loginButton;
    private ConstraintLayout profileLayout;
    private ConstraintLayout signInLayout;
    private ProfileViewModel profileViewModel;
    private final String USER_PREFERENCE = "User";

    private FirebaseAuth auth;

    private FirebaseFirestore firestore;
    MyPreference myPreference ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        nameText = root.findViewById(R.id.nameText);
        signInLayout = root.findViewById(R.id.sigininlayout);
        profileLayout = root.findViewById(R.id.profilelayout);
        loginButton = root.findViewById(R.id.loginButton);
        logoutText = root.findViewById(R.id.logout);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       myPreference = new MyPreference(requireContext());
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getLoggedInUser().observe(getViewLifecycleOwner(), userDetails -> {

            if (userDetails != null) {
                signInLayout.setVisibility(View.GONE);

                myPreference.saveUserDetailsToSharedPreferences(userDetails.getName(),userDetails.getDocumentId());
                profileLayout.setVisibility(View.VISIBLE);

                String userName = myPreference.getUsername();
                String formattedName = userName != null ? userName : "";
                nameText.setText(formattedName);
            } else {
                signInLayout.setVisibility(View.VISIBLE);
                profileLayout.setVisibility(View.GONE);
            }
        });
        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profileViewModel.logout();
               myPreference.deleteUserFromSharedPreferences();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);

            }
        });
    }


}
