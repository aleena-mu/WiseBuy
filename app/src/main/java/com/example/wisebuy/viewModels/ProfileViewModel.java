package com.example.wisebuy.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wisebuy.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<User> loggedInUser = new MutableLiveData<>();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public LiveData<User> getLoggedInUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            User user = User.getInstance();
            loggedInUser.postValue(user);

            loggedInUser.postValue(user);
        } else {
            loggedInUser.postValue(null);
        }
        return loggedInUser;
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        // Clear the user details when logging out
        loggedInUser.postValue(null);
        User.getInstance().setPhoneNumber(null);
        User.getInstance().setName(null);
        User.getInstance().setPlace(null);
        loggedInUser.postValue(null);
    }
}
