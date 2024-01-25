package com.example.wisebuy.ui.allProducts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wisebuy.R;
import com.example.wisebuy.adapters.AllProductsAdapter;
import com.example.wisebuy.adapters.SliderAdapter;
import com.example.wisebuy.databinding.FragmentAllProductsBinding;
import com.example.wisebuy.model.AllProducts;
import com.example.wisebuy.services.AllProductsService;
import com.example.wisebuy.services.OfferService;

import java.util.ArrayList;
import java.util.List;


public class AllProductsFragment extends Fragment {

    private FragmentAllProductsBinding binding;
    RecyclerView allProductsView;
    AllProductsAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AllProductsViewModel allProductsViewModel =
                new ViewModelProvider(this).get(AllProductsViewModel.class);

        binding = FragmentAllProductsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
fetchAllProducts();

        allProductsView = root.findViewById(R.id.allProductsView);
        adapter = new AllProductsAdapter(requireContext(), new ArrayList<>());
        allProductsView.setLayoutManager(new LinearLayoutManager(requireContext()));
        allProductsView.setAdapter(adapter);

        return root;
    }
    private void fetchAllProducts() {
        AllProductsService.getProducts(
                productList -> {
                    adapter.setProductList(productList);


                },
                exception -> {
                    // Handle the error (e.g., show a message)
                    Toast.makeText(requireContext(), "Error fetching products", Toast.LENGTH_SHORT).show();
                }
        );
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}