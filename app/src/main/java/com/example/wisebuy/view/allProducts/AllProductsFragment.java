package com.example.wisebuy.view.allProducts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wisebuy.MainActivity;
import com.example.wisebuy.MyApplication;
import com.example.wisebuy.R;
import com.example.wisebuy.adapters.AllProductsAdapter;
import com.example.wisebuy.databinding.FragmentAllProductsBinding;
import com.example.wisebuy.repositories.AllProductsRepository;
import com.example.wisebuy.view.auth.LoginOtp;
import com.example.wisebuy.viewModels.AllProductsViewModel;

import java.util.ArrayList;


public class AllProductsFragment extends Fragment {

    private FragmentAllProductsBinding binding;
    RecyclerView allProductsView;
    AllProductsAdapter adapter,updatedAdapter;
    SearchView searchView;
    AllProductsViewModel allProductsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAllProductsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        searchView=root.findViewById(R.id.searchView);
        setupSearchView();
        allProductsView = root.findViewById(R.id.allProductsView);
        allProductsView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new AllProductsAdapter(requireContext(), new ArrayList<>());

        MyApplication myApplication = (MyApplication) requireActivity().getApplication();
        allProductsViewModel = myApplication.getAllProductsViewModel();
        allProductsViewModel.setAdapter(adapter);
        allProductsViewModel.loadProducts();
        allProductsViewModel.getAdapterUpdateLiveData().observe(getViewLifecycleOwner(), adapterUpdate -> {
            if(adapterUpdate) {
                updatedAdapter=allProductsViewModel.getAdapter();
                allProductsView.setAdapter(updatedAdapter);
            }
            else{
                Intent intent = new Intent(getContext(), NoProductsActivity.class);
               startActivity(intent);
            }
        });
        return root;
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                allProductsViewModel.searchProducts(query);
                allProductsViewModel.getSearchResultsLiveData().observe(getViewLifecycleOwner(),productsList -> {
                            if (!productsList.isEmpty()) {
                                adapter.setProductList(productsList);
                                allProductsView.setAdapter(updatedAdapter);
                            }
                            else{
                                Intent intent = new Intent(getContext(), NoProductsActivity.class);
                                startActivity(intent);
                            }
                        }
                );
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {return false;}
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
