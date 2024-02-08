package com.example.wisebuy.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wisebuy.adapters.AllProductsAdapter;
import com.example.wisebuy.models.AllProducts;
import com.example.wisebuy.repositories.AllProductsRepository;

import java.util.Collections;
import java.util.List;

public class AllProductsViewModel extends ViewModel {
    private final AllProductsRepository repository = new AllProductsRepository();
    private final MutableLiveData<Boolean> adapterUpdateLiveData = new MutableLiveData<>();
    private AllProductsAdapter adapter;
    private AllProductsAdapter searchAdapter;
    private String categorySearch;

    public AllProductsAdapter getSearchAdapter() {
        return searchAdapter;
    }

    public void setSearchAdapter(AllProductsAdapter searchAdapter) {
        this.searchAdapter = searchAdapter;
    }

    private final MutableLiveData<List<AllProducts>> searchResultsLiveData = new MutableLiveData<>();




    public LiveData<List<AllProducts>> getSearchResultsLiveData() {
        return searchResultsLiveData;
    }

    public void setAdapter(AllProductsAdapter adapter) {
        this.adapter = adapter;
    }

    public AllProductsAdapter getAdapter() {
        return adapter;

    }

    public LiveData<Boolean> getAdapterUpdateLiveData() {
        return adapterUpdateLiveData;
    }

    public void loadProducts() {
        AllProductsRepository.getProducts(
                productList -> {
                    if (adapter != null) {
                        adapter.setProductList(productList);
                        adapterUpdateLiveData.setValue(true);
                    }
                    else{
                        Log.d("AdaptorNull","AdaptorNull");
                    }

                },
                exception -> {
                    adapterUpdateLiveData.setValue(false);
                    }
        );
    }

    public void searchProducts(String query) {
        Log.d("NewProductListUP",query);
        repository.searchProducts(query,
                products -> {
                    searchResultsLiveData.postValue(products);
                    searchAdapter.setProductList(products); // Update adapter with search results

                },
                e -> {
                    searchResultsLiveData.postValue(Collections.emptyList());


                });
        }
    }

