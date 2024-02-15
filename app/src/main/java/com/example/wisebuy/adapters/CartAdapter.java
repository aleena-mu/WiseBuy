package com.example.wisebuy.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wisebuy.MyApplication;
import com.example.wisebuy.R;
import com.example.wisebuy.models.CartModel;
import com.example.wisebuy.repositories.CartRepository;
import com.example.wisebuy.util.AndroidUtil;
import com.example.wisebuy.viewModels.CartViewModel;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {


    private List<CartModel> cartList;
        private final Context context;


        private final CartRepository repository = new CartRepository();
        private final CartViewModel cartViewModel;

        public CartAdapter(Context context, List<CartModel> cartList,CartViewModel cartViewModel) {
            this.context = context;
            this.cartList = cartList;
            this.cartViewModel=cartViewModel;

        }

        @NonNull
        @Override
        public com.example.wisebuy.adapters.CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_design, parent, false);
            return new com.example.wisebuy.adapters.CartAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull com.example.wisebuy.adapters.CartAdapter.ViewHolder holder, int position) {
            CartModel cartModel = cartList.get(position);
            Glide.with(context)
                    .load(cartModel.getImageUrl())
                    .into(holder.cartPic);

            holder.cartTitle.setText(cartModel.getTitle());
            holder.cartItemPrice.setText(String.valueOf(cartModel.getPrice()));
            holder.itemQuantity.setText(String.valueOf(cartModel.getQuantity()));
            holder.cartItemTotal.setText(String.valueOf(cartModel.getTotalPriceofSingleItem()));
            holder.addItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                        holder.itemQuantity.setText(String.valueOf(cartModel.getQuantity() + 1));
                        cartViewModel.updateQuantity(cartModel.getQuantity() + 1,cartModel.getProductId());
                        Log.d("CartId",""+cartModel.getProductId());


                }
            });
            holder.deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cartModel.getQuantity() == 1) {

                    }
                    else {
                        holder.itemQuantity.setText(String.valueOf(cartModel.getQuantity() + 1));
                        cartViewModel.updateQuantity(cartModel.getQuantity() - 1, cartModel.getProductId());
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return cartList.size();
        }

        public void renewItems(List<CartModel> cartModel) {
            this.cartList = cartModel;
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView cartPic;
            TextView cartTitle, cartItemPrice,cartItemTotal,itemQuantity,addItem,deleteItem;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                cartPic = itemView.findViewById(R.id.cartPic);
                cartTitle = itemView.findViewById(R.id.cartTitle);
                cartItemPrice = itemView.findViewById(R.id.singleItemCost);
                cartItemTotal = itemView.findViewById(R.id.totalCostOfItem);
                itemQuantity = itemView.findViewById(R.id.itemQuantity);
                addItem=itemView.findViewById(R.id.addItem);
                deleteItem=itemView.findViewById(R.id.deleteItem);

            }
        }

        private void navigateToAllProductsFragment(String categoryTitle) {
            // Pass the category title as an argument to the AllProductsFragment
            Bundle bundle = new Bundle();
            bundle.putString("categoryTitle", categoryTitle);


            NavController navController = Navigation.findNavController((Activity) context, R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.action_navigation_home_to_navigation_all_products,bundle);
        }



    }



