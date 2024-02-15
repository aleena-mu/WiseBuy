package com.example.wisebuy.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wisebuy.adapters.AllProductsAdapter;
import com.example.wisebuy.adapters.CartAdapter;
import com.example.wisebuy.repositories.CartRepository;

public class CartViewModel extends ViewModel {
    private final MutableLiveData<Boolean> cartAdapterUpdateLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> subTotalLivedata = new MutableLiveData<>();

    private CartAdapter cartAdapter;


    public MutableLiveData<Integer> getSubTotalLivedata() {
        return subTotalLivedata;
    }

    public MutableLiveData<Boolean> getCartAdapterUpdateLiveData() {
        return cartAdapterUpdateLiveData;
    }

    public CartAdapter getCartAdapter() {
        return cartAdapter;
    }

    public void setCartAdapter(CartAdapter cartAdapter) {
        this.cartAdapter = cartAdapter;
    }

    public void addToCart(String userId, String productId){
        CartRepository.addToCart(userId,productId);


    }
    public  void  showCart(String userId){
        CartRepository.showCart(userId,
                totalSum-> {
                    subTotalLivedata.postValue(totalSum.intValue());
                },
                taskCart->{
                    if (!taskCart.isEmpty()) {
                        cartAdapter.renewItems(taskCart);
                        cartAdapterUpdateLiveData.postValue(true);
                    }
                    else{
                        cartAdapterUpdateLiveData.postValue(false);

                    }
                },
                exception-> {
                    cartAdapterUpdateLiveData.postValue(false);

        });


    }

    public void updateQuantity(int quantity,String cartId){
        CartRepository.updateQuantity(quantity,cartId);
}
}
