package com.example.wisebuy;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import androidx.lifecycle.ViewModelProvider;

import com.example.wisebuy.broadcastReceiver.NetworkReceiver;
import com.example.wisebuy.repositories.NetworkStateRepository;
import com.example.wisebuy.viewModels.HomeViewModel;
import com.example.wisebuy.viewModels.AllProductsViewModel;
import com.example.wisebuy.viewModels.LoginViewModel;


public class MyApplication extends Application {
    private LoginViewModel loginViewModel;
    private AllProductsViewModel allProductsViewModel;
    private HomeViewModel homeViewModel;
    private NetworkStateRepository networkStateRepository;



    @Override
    public void onCreate() {
        super.onCreate();

        loginViewModel = new ViewModelProvider.AndroidViewModelFactory(this).create(LoginViewModel.class);
        allProductsViewModel = new ViewModelProvider.AndroidViewModelFactory(this).create(AllProductsViewModel.class);
        homeViewModel = new ViewModelProvider.AndroidViewModelFactory(this).create(HomeViewModel.class);

    }

    public LoginViewModel getAuthViewModel() {
        return loginViewModel;
    }

    public AllProductsViewModel getAllProductsViewModel() {
        return allProductsViewModel;
    }
    public HomeViewModel getHomeViewModel() {
        return homeViewModel;
    }


    public NetworkStateRepository getNetworkStateRepository() {
        return networkStateRepository;
    }

}
