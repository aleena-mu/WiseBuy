package com.example.wisebuy.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import com.example.wisebuy.R;
import com.example.wisebuy.models.HomeScreenDeals;


import java.util.List;

public class HomeScreenDealsAdapter extends RecyclerView.Adapter<HomeScreenDealsAdapter.ViewHolder> {

    private List<HomeScreenDeals> dealsList;
    private final Context context;
    private String dealSearch;

    public HomeScreenDealsAdapter(Context context, List<HomeScreenDeals> dealsList) {
        this.context = context;
        this.dealsList = dealsList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_screen_deals, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeScreenDeals topDeals = dealsList.get(position);
        Glide.with(context)
                .load(topDeals.getImageURL())
                .into(holder.dealIcon);

        holder.dealCategory.setText(topDeals.getTitle());
        holder.dealDetail.setText(topDeals.getDeal());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealSearch=topDeals.getTitle();
                navigateToAllProductsFragment(dealSearch);

            }
        });
    }

    @Override
    public int getItemCount() {
        return dealsList.size();
    }
    public void renewItems(List<HomeScreenDeals> topDeals) {
        this.dealsList = topDeals;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView dealIcon;
        TextView dealCategory;
        TextView dealDetail;
//        TextView dealType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dealIcon = itemView.findViewById(R.id.productImage);
            dealCategory = itemView.findViewById(R.id.dealCategory);
            dealDetail = itemView.findViewById(R.id.topDealDetail);
//            dealType = itemView.findViewById(R.id.dealType);
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
