package com.example.wisebuy.ui.allProducts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AllProductsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AllProductsViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}