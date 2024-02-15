package com.example.wisebuy.view.cart;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wisebuy.MainActivity;
import com.example.wisebuy.MyApplication;
import com.example.wisebuy.MyPreference;
import com.example.wisebuy.R;
import com.example.wisebuy.adapters.CartAdapter;
import com.example.wisebuy.adapters.CategoryAdapter;
import com.example.wisebuy.view.auth.LoginActivity;
import com.example.wisebuy.viewModels.CartViewModel;
import com.example.wisebuy.viewModels.ProfileViewModel;
import com.google.protobuf.StringValue;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartFragment extends Fragment {


    private TextView subTotalTextView, deliveryFeeTextView, taxTextView, totalTextView;
    private RecyclerView cartView;
    private CartViewModel cartViewModel;
    MyPreference myPreference ;
    private CartAdapter cartAdapter;
    LinearLayout cartItemsView,cartEmptyView;
    int deliveryFee;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);
        cartView = root.findViewById(R.id.cartView);
        cartView.setLayoutManager(new LinearLayoutManager(getActivity()));

        cartItemsView=root.findViewById(R.id.cartItems);
        cartEmptyView=root.findViewById(R.id.cartEmpty);
        subTotalTextView=root.findViewById(R.id.subTotalText);
        deliveryFeeTextView=root.findViewById(R.id.deliveryFeeText);
        taxTextView=root.findViewById(R.id.taxText);
        totalTextView=root.findViewById(R.id.totalText);

        cartEmptyView.setVisibility(View.GONE);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MyApplication myApplication = (MyApplication) requireActivity().getApplication();
        cartViewModel = myApplication.getCartViewModel();
        myPreference = new MyPreference(requireContext());
        cartAdapter=new CartAdapter(requireContext(),new ArrayList<>(),cartViewModel);
        cartViewModel.setCartAdapter(cartAdapter);

            cartViewModel.showCart(myPreference.getUserId());
            cartViewModel.getCartAdapterUpdateLiveData().observe(getViewLifecycleOwner(),cartItems->{
                if(!cartItems){
                    cartItemsView.setVisibility(View.GONE);
                    cartEmptyView.setVisibility(View.VISIBLE);
                }
                else{
                    cartAdapter=cartViewModel.getCartAdapter();
                    cartView.setAdapter(cartAdapter);
                    cartViewModel.getSubTotalLivedata().observe(getViewLifecycleOwner(),subtotal->{
                        subTotalTextView.setText("₹"+String.valueOf(subtotal));
                        if(subtotal<5000000){
                           deliveryFee=40;
                        }
                        else{
                            deliveryFee=0;
                        }
                        String deliveryFeeText="₹"+String.valueOf(deliveryFee);
                        deliveryFeeTextView.setText(deliveryFeeText);
                        int tax=(int)(subtotal*0.2);
                        String taxText="₹"+String.valueOf(tax);
                        taxTextView.setText(taxText);
                        int grandTotal= (int)(subtotal+deliveryFee+tax);
                        String totalText="₹"+String.valueOf(grandTotal);
                        totalTextView.setText(totalText);

                    });

                }
        });

    }


}
