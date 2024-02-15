package com.example.wisebuy;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;

import com.example.wisebuy.repositories.NetworkStateRepository;
import com.example.wisebuy.viewModels.CartViewModel;
import com.example.wisebuy.viewModels.HomeViewModel;
import com.example.wisebuy.viewModels.AllProductsViewModel;
import com.example.wisebuy.viewModels.LoginViewModel;


public class MyApplication extends Application {
    private LoginViewModel loginViewModel;
    private AllProductsViewModel allProductsViewModel;
    private HomeViewModel homeViewModel;
    private CartViewModel cartViewModel;
    private NetworkStateRepository networkStateRepository;



    @Override
    public void onCreate() {
        super.onCreate();

        loginViewModel = new ViewModelProvider.AndroidViewModelFactory(this).create(LoginViewModel.class);
        allProductsViewModel = new ViewModelProvider.AndroidViewModelFactory(this).create(AllProductsViewModel.class);
        homeViewModel = new ViewModelProvider.AndroidViewModelFactory(this).create(HomeViewModel.class);
        cartViewModel = new ViewModelProvider.AndroidViewModelFactory(this).create(CartViewModel.class);

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
    public CartViewModel getCartViewModel() {
        return cartViewModel;
   }


    public NetworkStateRepository getNetworkStateRepository() {
        return networkStateRepository;
    }

}
