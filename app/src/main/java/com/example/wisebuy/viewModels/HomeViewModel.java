package com.example.wisebuy.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wisebuy.adapters.AllProductsAdapter;
import com.example.wisebuy.adapters.CategoryAdapter;
import com.example.wisebuy.adapters.HomeScreenDealsAdapter;
import com.example.wisebuy.repositories.AllProductsRepository;
import com.example.wisebuy.repositories.CategoryRepository;
import com.example.wisebuy.repositories.HomeRepository;

public class HomeViewModel extends ViewModel {


    private final CategoryRepository repository = new CategoryRepository();
    private final MutableLiveData<Boolean> adapterUpdateLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> dealAdapterLiveData = new MutableLiveData<>();
    private CategoryAdapter adapter;
    private HomeScreenDealsAdapter dealadapter;

    public MutableLiveData<Boolean> getDealAdapterLiveData() {
        return dealAdapterLiveData;
    }

    public HomeScreenDealsAdapter getDealadapter() {
        return dealadapter;
    }

    public void setDealadapter(HomeScreenDealsAdapter dealadapter) {
        this.dealadapter = dealadapter;
    }

    public MutableLiveData<Boolean> getAdapterUpdateLiveData() {
        return adapterUpdateLiveData;
    }

    public CategoryAdapter getAdapter() {
        return adapter;
    }





    public void setAdapter(CategoryAdapter adapter) {
        this.adapter = adapter;
    }

    public void loadCategories() {
        CategoryRepository.getCategories(
                categoryModelList -> {
                    Log.d("HomeViewModel", "Categories loaded: " + categoryModelList.size());
                    if (adapter != null) {
                        adapter.renewItems(categoryModelList);
                        adapterUpdateLiveData.setValue(true);
                    }
                },
                exception -> {
                    Log.e("HomeViewModel", "Error loading categories", exception);
                    adapterUpdateLiveData.setValue(false);
                }
        );
    }
    public void loadTopDeals(){
        HomeRepository.getTopDeals(topDealList-> {
            if(dealadapter!=null){
                dealadapter.renewItems(topDealList);
                dealAdapterLiveData.postValue(true);
            }
            },
                e->{
                    Log.e("HomeViewModel", "Error loading categories", e);
                    dealAdapterLiveData.setValue(false);


        });
    }
}