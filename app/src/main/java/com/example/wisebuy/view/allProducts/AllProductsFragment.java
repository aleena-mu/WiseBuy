package com.example.wisebuy.view.allProducts;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.wisebuy.MyApplication;
import com.example.wisebuy.MyPreference;
import com.example.wisebuy.R;
import com.example.wisebuy.adapters.AllProductsAdapter;
import com.example.wisebuy.adapters.ProductSliderAdapter;
import com.example.wisebuy.databinding.FragmentAllProductsBinding;
import com.example.wisebuy.models.AllProducts;
import com.example.wisebuy.viewModels.AllProductsViewModel;
import com.example.wisebuy.viewModels.CartViewModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class AllProductsFragment extends Fragment {

    private FragmentAllProductsBinding binding;
    private GridView allProductsView;
    private AllProductsAdapter updatedAdapter;
    private SearchView searchView;
    private AllProductsViewModel allProductsViewModel;
   private CartViewModel cartViewModel;
    private String categorySearch;
    MyPreference myPreference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAllProductsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchView = root.findViewById(R.id.searchView);
        LinearLayout productDetailsView = root.findViewById(R.id.productDetails);
        productDetailsView.setVisibility(View.GONE);
        MyApplication myApplication = (MyApplication) requireActivity().getApplication();

        allProductsViewModel = myApplication.getAllProductsViewModel();
        cartViewModel = myApplication.getCartViewModel();
        //cartViewModel=myApplication.getCartViewModel();
       myPreference= new MyPreference(requireContext());
        setupSearchView();

        Bundle bundle = getArguments();
        if (bundle != null) {
            categorySearch = bundle.getString("categoryTitle", "");
        }

        allProductsView = root.findViewById(R.id.allProductsView);
        allProductsView.setOnItemClickListener((parent, view, position, id) -> {
            AllProducts clickedProduct = (AllProducts) parent.getItemAtPosition(position);
            Log.d("AllProductsFragment", "Item clicked: " + clickedProduct.getDocumentId());
            productDetailsView.setVisibility(View.VISIBLE);
            allProductsView.setVisibility(View.GONE);
            Button addToCartButton=root.findViewById(R.id.addToCartButton);
            SliderView productImageSlider = root.findViewById(R.id.productImageSlider);
            TextView titleText = root.findViewById(R.id.titleText);
            TextView descriptionText = root.findViewById(R.id.descriptionText);
            TextView priceText = root.findViewById(R.id.priceText);
            TextView brandText = root.findViewById(R.id.brandText);

            allProductsViewModel.getProductDetails(clickedProduct.getDocumentId());
            allProductsViewModel.getProductDetailsLiveData().observe(getViewLifecycleOwner(), productDetails -> {
                if (productDetails != null) {
                    ProductSliderAdapter sliderAdapter = new ProductSliderAdapter(getContext(), productDetails.getImageUrls());
                    productImageSlider.setSliderAdapter(sliderAdapter);
                    productImageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
                    productImageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                    productImageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                    productImageSlider.setIndicatorSelectedColor(Color.WHITE);
                    productImageSlider.setIndicatorUnselectedColor(Color.GRAY);
                    productImageSlider.setScrollTimeInSec(3);
                    productImageSlider.setSliderAdapter(sliderAdapter);
                    titleText.setText(productDetails.getTitle());
                    descriptionText.setText(productDetails.getDetails());
                    priceText.setText("Price: ₹"+ String.valueOf(productDetails.getPrice()));
                    brandText.setText("Brand: "+productDetails.getBrand());

                    addToCartButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            cartViewModel.addToCart(myPreference.getUserId(),clickedProduct.getDocumentId());

                        }
                    });
                } else {
                    Toast.makeText(getContext(), "NoContent", Toast.LENGTH_SHORT).show();
                }
            });
        });

        AllProductsAdapter adapter = new AllProductsAdapter(requireContext(), new ArrayList<>());
        AllProductsAdapter categoryAdapter = new AllProductsAdapter(requireContext(), new ArrayList<>());


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
                        Toast.makeText(requireContext(), "No such products", Toast.LENGTH_SHORT).show();
                    }
                });
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
