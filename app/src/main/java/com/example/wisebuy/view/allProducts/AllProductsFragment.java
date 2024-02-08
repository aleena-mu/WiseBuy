package com.example.wisebuy.view.allProducts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.wisebuy.MyApplication;
import com.example.wisebuy.R;
import com.example.wisebuy.adapters.AllProductsAdapter;
import com.example.wisebuy.databinding.FragmentAllProductsBinding;
import com.example.wisebuy.viewModels.AllProductsViewModel;

import java.util.ArrayList;


public class AllProductsFragment extends Fragment {

    private FragmentAllProductsBinding binding;
    private GridView allProductsView;
    private AllProductsAdapter updatedAdapter;

    private SearchView searchView;
    private AllProductsViewModel allProductsViewModel;
    private String categorySearch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAllProductsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        searchView = root.findViewById(R.id.searchView);


        setupSearchView();

        Bundle bundle = getArguments();
        if (bundle != null) {
            categorySearch = bundle.getString("categoryTitle", "");
            Log.d("QueyinRep0", "categorySearch" + categorySearch);
        }
        allProductsView = root.findViewById(R.id.allProductsView);

        AllProductsAdapter adapter = new AllProductsAdapter(requireContext(), new ArrayList<>());
        AllProductsAdapter categoryAdapter = new AllProductsAdapter(requireContext(), new ArrayList<>());

        MyApplication myApplication = (MyApplication) requireActivity().getApplication();
        allProductsViewModel = myApplication.getAllProductsViewModel();
        allProductsViewModel.setAdapter(adapter);
        allProductsViewModel.setSearchAdapter(adapter);
        allProductsViewModel.loadProducts();

        if (categorySearch != null) {
            allProductsViewModel.searchProducts(categorySearch);
            allProductsViewModel.getSearchResultsLiveData().observe(getViewLifecycleOwner(), productList -> {
                if (!productList.isEmpty()) {
                    categoryAdapter.setProductList(productList);
                    allProductsView.setAdapter(categoryAdapter);
                }
            });
        } else {

            allProductsViewModel.getAdapterUpdateLiveData().observe(getViewLifecycleOwner(), adapterUpdate -> {
                if (adapterUpdate) {
                    updatedAdapter = allProductsViewModel.getAdapter();
                    allProductsView.setAdapter(updatedAdapter);
                } else {
                    Toast.makeText(requireContext(), "No such products", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return root;
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                allProductsViewModel.searchProducts(query);
                allProductsViewModel.getSearchResultsLiveData().observe(getViewLifecycleOwner(), productsList -> {
                            if (!productsList.isEmpty()) {
                                updatedAdapter = allProductsViewModel.getSearchAdapter();
                                allProductsView.setAdapter(updatedAdapter);
                            } else {
                                Toast.makeText(requireContext(), "No such products", Toast.LENGTH_SHORT);
                            }
                        }
                );

                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
